package com.carrier.modelexposer.classifier.finegray;

import com.carrier.modelexposer.classifier.Classifier;
import com.carrier.modelexposer.exception.InvalidDoubleException;
import com.carrier.modelexposer.exception.InvalidIntegerException;
import com.carrier.modelexposer.exception.MissingAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import com.carrier.modelexposer.webservice.domain.ReducedRiskResponse;
import com.carrier.modelexposer.webservice.domain.RiskResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.carrier.modelexposer.util.Util.*;

public class FineGrayClassifier extends Classifier {
    private Map<String, Map<String, Double>> riskTable;

    @Override
    public RiskResponse classify(Map<String, String> evidence) throws Exception {
        double score = baseLine(evidence);

        RiskResponse response = createRiskResponse(score);
        return response;
    }

    @Override
    @SuppressWarnings ("checkstyle:magicNumber")
    public ReducedRiskResponse compareClassifications(Map<String, String> evidence, Map<String, String> comparison)
            throws Exception {


        ReducedRiskResponse result = new ReducedRiskResponse();
        result.setBaseline(classify(evidence));

        Map<String, String> evidences = new HashMap<>();
        evidences.putAll(evidence);
        evidences.putAll(comparison);

        double original = intervention(evidence);
        double intervention = intervention(evidences);
        double baseline = baseLine(evidence);
        double probability = intervention / original * baseline;

        if (getOptionalDoubleValue(comparison, "SBP") != null && getOptionalDoubleValue(comparison, "LDL") != null) {
            double sbp = getDoubleValue(evidence, "SBP");
            double targetSbp = getDoubleValue(comparison, "SBP");
            double ldl = getDoubleValue(evidence, "LDL");
            double targetLdl = getDoubleValue(comparison, "LDL");
            double ldlFactor = Math.pow(0.78, (ldl - targetLdl));
            if (targetLdl > 2.5) {
                ldlFactor = 1;
            }

            if (!containsOtherVariables(comparison)) {
                probability = baseLine(evidence) * Math.pow(0.8, (sbp - targetSbp) / 10) * ldlFactor;
            } else {
                probability *= Math.pow(0.8, (sbp - targetSbp) / 10) * ldlFactor;
            }
        } else if (getOptionalDoubleValue(comparison, "SBP") != null) {
            double sbp = getDoubleValue(evidence, "SBP");
            double targetSbp = getDoubleValue(comparison, "SBP");
            if (!containsOtherVariables(comparison)) {
                probability = baseline * Math.pow(0.8, (sbp - targetSbp) / 10);
            } else {
                probability *= Math.pow(0.8, (sbp - targetSbp) / 10);
            }
        } else if (getOptionalDoubleValue(comparison, "LDL") != null) {
            double ldl = getDoubleValue(evidence, "LDL");
            double targetLdl = getDoubleValue(comparison, "LDL");
            if (targetLdl <= 2.5) {
                if (!containsOtherVariables(comparison)) {
                    probability = baseline * Math.pow(0.78, (ldl - targetLdl));
                } else {
                    probability *= Math.pow(0.78, (ldl - targetLdl));
                }
            }
        }
        Map<String, Double> prob = new HashMap<>();
        prob.put("CVD", probability);
        result.setResult(comparison, prob);

        return result;
    }

    @SuppressWarnings ("checkstyle:magicNumber")
    private double baseLine(Map<String, String> evidence)
            throws UnknownStateException, InvalidDoubleException, InvalidIntegerException, MissingAttributeException {
        Boolean exSmoker = getOptionalBooleanValue(evidence, "ex_smoker");
        Boolean antihypertensives = getOptionalBooleanValue(evidence, "antihypertensives");
        Boolean betaBlockingAgents = getOptionalBooleanValue(evidence, "beta_blocking_agents");
        Boolean calciumChannelBlockers = getOptionalBooleanValue(evidence, "calcium_channel_blockers");
        Boolean rASInhibitors = getOptionalBooleanValue(evidence, "RAS_inhibitors");
        Boolean lipidModifyingAgents = getOptionalBooleanValue(evidence, "lipid_modifying_agents");
        Double champScore = calcChampScore(evidence) / 60; //fine gray wants the score in hours
        Integer eetscore = getIntValue(evidence, "eetscore");
        Double seswoa = getDoubleValue(evidence, "seswoa");

        int smokingYes = 0;

        String gender = getStringValue(evidence, "gender");

        int age = getIntValue(evidence, "age");
        String smoking = getStringValue(evidence, "current_smoker");
        double sbp = getDoubleValue(evidence, "SBP");
        double tc = getDoubleValue(evidence, "TC");
        double hdl = getDoubleValue(evidence, "HDL");

        if (smoking.equals("yes")) {
            smokingYes = 1;
        } else if (smoking.equals("no")) {
            smokingYes = 0;
        } else {
            throw new UnknownStateException(smoking, "current_smoker", "'yes', 'no'");
        }


        int genderInt = 0;
        if (gender.equals("male")) {
            genderInt = 1;
        }

        Boolean diabetes = (getOptionalBooleanValue(evidence, "diabetes_type_1") != null && getOptionalBooleanValue(
                evidence, "diabetes_type_1"))
                || (getOptionalBooleanValue(evidence,
                                            "diabetes_type_2") != null
                && getOptionalBooleanValue(evidence, "diabetes_type_2"))
                || (getOptionalBooleanValue(evidence, "diabetes_other") != null
                && getOptionalBooleanValue(evidence, "diabetes_other"));
        int diabetesInt = 0;
        if (diabetes) {
            diabetesInt = 1;
        }

        int exSmokerInt = 0;
        if (exSmoker != null && exSmoker) {
            exSmokerInt = 1;
        }

        int lipidModifyingAgentsInt = 0;
        if (lipidModifyingAgents) {
            lipidModifyingAgentsInt = 1;
        }

        int bpMeds = 0;
        if ((antihypertensives != null && antihypertensives) || (betaBlockingAgents
                != null && betaBlockingAgents) || (calciumChannelBlockers != null && calciumChannelBlockers)
                || (rASInhibitors != null && rASInhibitors)) {
            bpMeds *= 1.1;
        }
        int preexistingCVD = 0;  // this is always 0, cuz exclusion criteria


        return (1 - (Math.pow((1 - 0.02224657), Math.exp(
                0.05873692 * (age - 59.63795) + 0.46032031 * genderInt + 0.39404609 * diabetesInt + 0.68281002
                        * preexistingCVD + -0.41203031 * (seswoa - 0.0602643) + 0.74567125 * smokingYes + 0.15477294
                        * exSmokerInt + -0.00120839 * (champScore - 5.433716) + -0.00231330 * (eetscore - 84.15415)
                        + 0.00808079 * (sbp - 133.0301) + 0.12838867 * (tc - 5.220962) + -0.47619288 * (hdl - 1.557978)
                        + 0.42483205 * bpMeds + -0.34413909 * lipidModifyingAgentsInt))));

    }

    @SuppressWarnings ("checkstyle:magicNumber")
    private double intervention(Map<String, String> evidence)
            throws UnknownStateException, InvalidDoubleException, InvalidIntegerException, MissingAttributeException {
        Boolean exSmoker = getOptionalBooleanValue(evidence, "ex_smoker");
        Double champScore = calcChampScore(evidence) / 60; //finegray wants it in minutes
        Integer eetscore = getIntValue(evidence, "eetscore");
        double calcChampScoreInterventionModifier = calcChampScoreIntervention(champScore, evidence);


        Double seswoa = getDoubleValue(evidence, "seswoa");
        int smokingYes = 0;

        String gender = getStringValue(evidence, "gender");

        int age = getIntValue(evidence, "age");
        String smoking = getStringValue(evidence, "current_smoker");

        if (smoking.equals("yes")) {
            smokingYes = 1;
        } else if (smoking.equals("no")) {
            smokingYes = 0;
        } else {
            throw new UnknownStateException(smoking, "current_smoker", "'yes', 'no'");
        }


        int genderInt = 0;
        if (gender.equals("male")) {
            genderInt = 1;
        }

        Boolean diabetes = (getOptionalBooleanValue(evidence, "diabetes_type_1") != null && getOptionalBooleanValue(
                evidence, "diabetes_type_1"))
                || (getOptionalBooleanValue(evidence,
                                            "diabetes_type_2") != null
                && getOptionalBooleanValue(evidence, "diabetes_type_2"))
                || (getOptionalBooleanValue(evidence, "diabetes_other") != null
                && getOptionalBooleanValue(evidence, "diabetes_other"));
        int diabetesInt = 0;
        if (diabetes) {
            diabetesInt = 1;
        }

        int exSmokerInt = 0;
        if (exSmoker != null && exSmoker) {
            exSmokerInt = 1;
        }


        int preexistingCVD = 0; // this is always 0, cuz exclusion criteria


        //modifying entire risk with calcChampScoreInterventionModifier

        return calcChampScoreInterventionModifier * (1 - (Math.pow((1 - 0.02369760), Math.exp(
                0.06263895 * (age - 59.63795) + 0.58698845 * genderInt + 0.43360962 * diabetesInt + 0.64925821
                        * preexistingCVD + -0.43304302 * (seswoa - 0.0602643) + 0.75612791 * smokingYes + 0.17357978
                        * exSmokerInt + -0.00606962 * (champScore - 5.433716) + -0.00293096 * (eetscore - 84.15415)))));


    }

    private double calcChampScoreIntervention(Double champScore, Map<String, String> evidence)
            throws UnknownStateException {
        String interventionExcercise = getOptionalStringValue(evidence, "intervention_exercise");
        if (interventionExcercise == null) {
            return 1;
        }
        checkValidInterventionExcercise(interventionExcercise);
        return getRiskTable().get(calcChampScoreIQuartile(champScore))
                .get(interventionExcercise);
    }

    @SuppressWarnings ("checkstyle:magicNumber")
    private void checkValidInterventionExcercise(String interventionExcercise) throws UnknownStateException {
        List<String> valid = Arrays.asList("0_3", "3_4.75", "4.75_8", ">8");
        if (!valid.contains(interventionExcercise)) {
            String s = "";
            for (String v : valid) {
                if (s.length() > 0) {
                    s += " ";
                }
                s += "'" + v + "'";
            }
            throw new UnknownStateException(interventionExcercise, "intervention_exercise", s);
        }
    }

    private Map<String, Map<String, Double>> getRiskTable() {
        if (riskTable == null) {
            riskTable = createRiskTable();
        }
        return riskTable;
    }

    @SuppressWarnings ("checkstyle:magicNumber")
    private Map<String, Map<String, Double>> createRiskTable() {
        Map<String, Map<String, Double>> riskTable = new HashMap<>();
        Map<String, Double> interventions0U3 = new HashMap<>();

        interventions0U3.put("0_3", 1.0);
        interventions0U3.put("3_4.75", 0.79);
        interventions0U3.put("4.75_8", 0.72);
        interventions0U3.put(">8", 0.60);

        riskTable.put("0_3", interventions0U3);

        Map<String, Double> interventions3U475 = new HashMap<>();

        interventions3U475.put("0_3", 1.27);
        interventions3U475.put("3_4.75", 1.0);
        interventions3U475.put("4.75_8", 0.91);
        interventions3U475.put(">8", 0.76);

        riskTable.put("3_4.75", interventions3U475);

        Map<String, Double> interventions475U8 = new HashMap<>();

        interventions475U8.put("0_3", 1.39);
        interventions475U8.put("3_4.75", 1.1);
        interventions475U8.put("4.75_8", 1.0);
        interventions475U8.put(">8", 0.83);

        riskTable.put("4.75_8", interventions475U8);

        Map<String, Double> interventions8 = new HashMap<>();

        interventions8.put("0_3", 1.67);
        interventions8.put("3_4.75", 1.32);
        interventions8.put("4.75_8", 1.2);
        interventions8.put(">8", 1.0);

        riskTable.put(">8", interventions8);

        return riskTable;
    }

    private RiskResponse createRiskResponse(double risk) {
        RiskResponse response = new RiskResponse();
        Map<String, Double> probabilities = new HashMap<>();
        probabilities.put("CVD", risk);
        response.setProbabilities(probabilities);
        return response;
    }

    private boolean containsOtherVariables(Map<String, String> comparison) {
        for (String key : comparison.keySet()) {
            if (!(key.equals("SBP") || key.equals("LDL"))) {
                return true;
            }
        }
        return false;
    }
}
