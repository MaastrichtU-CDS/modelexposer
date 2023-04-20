package com.carrier.modelexposer.classifier.score2;

import com.carrier.modelexposer.classifier.Classifier;
import com.carrier.modelexposer.exception.InvalidDoubleException;
import com.carrier.modelexposer.exception.InvalidIntegerException;
import com.carrier.modelexposer.exception.MissingAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import com.carrier.modelexposer.webservice.domain.ReducedRiskResponse;
import com.carrier.modelexposer.webservice.domain.RiskResponse;

import java.util.HashMap;
import java.util.Map;

import static com.carrier.modelexposer.util.Util.*;

public class Score2Classifier extends Classifier {

    public RiskResponse classify(Map<String, String> evidence)
            throws MissingAttributeException, InvalidIntegerException, InvalidDoubleException, UnknownStateException {
        double score2 = score2(evidence);

        score2 = manipulateScore2(evidence, score2);

        RiskResponse response = createRiskResponse(score2);
        return response;
    }

    @SuppressWarnings ("checkstyle:magicNumber")
    private double manipulateScore2(Map<String, String> evidence, double score2)
            throws UnknownStateException, InvalidIntegerException, InvalidDoubleException {
        //random nonsense to give the attributes not yet included an effect for testing purposes.
        Boolean exSmoker = getOptionalBooleanValue(evidence, "ex_smoker");
        Boolean antihypertensives = getOptionalBooleanValue(evidence, "antihypertensives");
        Boolean betaBlockingAgents = getOptionalBooleanValue(evidence, "beta_blocking_agents");
        Boolean calciumChannelBlockers = getOptionalBooleanValue(evidence, "calcium_channel_blockers");
        Boolean rASInhibitors = getOptionalBooleanValue(evidence, "RAS_inhibitors");
        Boolean lipidModifyingAgents = getOptionalBooleanValue(evidence, "lipid_modifying_agents");
        Double champScore = calcChampScore(evidence);
        Integer eetscore = getOptionalIntValue(evidence, "eetscore");
        Double ldl = getOptionalDoubleValue(evidence, "ldl");

        if (exSmoker != null && exSmoker) {
            score2 *= 1.01;
        }
        if (antihypertensives != null && antihypertensives) {
            score2 *= 1.02;
        }
        if (betaBlockingAgents != null && betaBlockingAgents) {
            score2 *= 1.03;
        }
        if (calciumChannelBlockers != null && calciumChannelBlockers) {
            score2 *= 1.04;
        }
        if (rASInhibitors != null && rASInhibitors) {
            score2 *= 1.05;
        }
        if (lipidModifyingAgents != null && lipidModifyingAgents) {
            score2 *= 1.06;
        }
        if (champScore != null) {
            if (champScore > 200) {
                score2 *= 0.8;
            } else if (champScore < 100) {
                score2 *= 1.02;
            }
        }
        if (eetscore != null) {
            if (eetscore > 85) {
                score2 *= 0.9;
            } else if (eetscore < 65) {
                score2 *= 1.02;
            }
        }
        if (ldl != null) {
            score2 *= 1 - (ldl / 7); // 3.5 is max value, so max reduction of 50% based on ldl
        }

        //max score if all modifiers active:
        // 1.01 * 1.02 * 1.03 * 1.04 * 1.05 * 1.06 * 1.02 * 1.02 = 1.27
        // so normalize
        score2 /= 1.27;


        return score2;

    }

    @Override
    public ReducedRiskResponse compareClassifications(Map<String, String> evidence,
                                                      Map<String, String> comparison)
            throws InvalidIntegerException, InvalidDoubleException,
                   MissingAttributeException, UnknownStateException {
        ReducedRiskResponse result = new ReducedRiskResponse();
        result.setBaseline(classify(evidence));

        Map<String, String> evidences = new HashMap<>();
        evidences.putAll(evidence);
        evidences.putAll(comparison);
        result.setResult(comparison, classify(evidences).getProbabilities());
        return result;
    }

    @SuppressWarnings ("checkstyle:magicNumber") //ignore magic numbers in the score-function
    public double score2(Map<String, String> evidence)
            throws MissingAttributeException, InvalidIntegerException, InvalidDoubleException, UnknownStateException {
        //Collect relevant attributes
        //Score function in the following DOI: 10.1093/eurheartj/ehab309
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

        if (gender.equals("male")) {
            double lp =
                    0.3742 * (age - 60) / 5 + 0.6012 * smokingYes + 0.2777 * (sbp - 120) / 20 + 0.1458
                            * (tc - 6) + -0.2698 * (hdl - 1.3) * 2 + -0.0755 * (age - 60) / 5 * smokingYes + -0.0255
                            * (age - 60) / 5 * (sbp - 120) / 20 + -0.0281 * (age - 60) / 5 * (tc - 6) + 0.0426
                            * (age - 60) / 5 * (hdl - 1.3) * 2;
            double unscaledPred = 1 - Math.pow(0.9605, Math.exp(lp));

            return (1 - Math.exp(-1 * Math.exp(-0.5699 + 0.7476 * Math.log(-1 * Math.log(1 - unscaledPred)))));
        } else if (gender.equals("female")) {
            double lp =
                    0.4648 * (age - 60) / 5 + 0.7744 * smokingYes + 0.3131 * (sbp - 120) / 20 + 0.1002
                            * (tc - 6) + -0.2606 * (hdl - 1.3) * 2 + -0.1088 * (age - 60) / 5 * smokingYes + -0.0277
                            * (age - 60) / 5 * (sbp - 120) / 20 + -0.0226 * (age - 60) / 5 * (tc - 6) + 0.0613
                            * (age - 60) / 5 * (hdl - 1.3) * 2;
            double unscaledPred = 1 - Math.pow(0.9776, Math.exp(lp));

            return (1 - Math.exp(-1 * Math.exp(-0.7380 + 0.7019 * Math.log(-1 * Math.log(1 - unscaledPred)))));
        } else {
            throw new UnknownStateException(gender, "gender", "'male', 'female'");
        }
    }

    private RiskResponse createRiskResponse(double risk) {
        RiskResponse response = new RiskResponse();
        Map<String, Double> probabilities = new HashMap<>();
        probabilities.put("CVD", risk);
        response.setProbabilities(probabilities);
        return response;
    }
}
