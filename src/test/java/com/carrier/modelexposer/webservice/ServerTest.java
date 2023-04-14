package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.webservice.domain.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {

    @Test
    public void testClassifyTestCorrect()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("current_smoker", "yes");
            evidence.put("current_smoker_cigarette", "no");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            RiskResponse response = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response.getProbabilities().size(), 1);

            assertEquals(response.getProbabilities().get("CVD"), 0.085, 0.001);
        }
    }

    @Test
    public void testClassifyTestCorrectNullValue()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("current_smoker", "yes");
            evidence.put("current_smoker_cigarette", "no");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "null");
            evidence.put("weight", "null");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            RiskResponse response = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response.getProbabilities().size(), 1);

            assertEquals(response.getProbabilities().get("CVD"), 0.085, 0.001);
        }
    }

    @Test
    public void testClassifyTestAllTheAttributesCorrect()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("arthritis_psoriatica", "yes");
            evidence.put("education", "level_1");
            evidence.put("current_smoker_other_years", "12");
            evidence.put("SLE", "yes");
            evidence.put("Glu", "12.0");
            evidence.put("diabetes_type_1", "yes");
            evidence.put("diabetes_type_2", "yes");
            evidence.put("household_size", "12");
            evidence.put("trauma_stressor_disorder", "yes");
            evidence.put("difficulties_household_income", "very_difficult");
            evidence.put("current_smoker_cigarette", "yes");
            evidence.put("eetscore", "66");
            evidence.put("height", "1");
            evidence.put("alcohol", "18");
            evidence.put("current_smoker_cigar_number_per_week", "12");
            evidence.put("address_postcode", "1234AB");
            evidence.put("RAS_inhibitors", "yes");
            evidence.put("steroid_tablets", "yes");
            evidence.put("obstructive_sleep_apnoea", "yes");
            evidence.put("current_smoker_pipe", "yes");
            evidence.put("CVD", "yes");
            evidence.put("PCOS", "yes");
            evidence.put("current_smoker_pipe_years", "12");
            evidence.put("bipolar_disorder", "yes");
            evidence.put("lipid_modifying_agents", "yes");
            evidence.put("CVD_family", "yes");
            evidence.put("diabetes_other", "yes");
            evidence.put("gender", "male");
            evidence.put("ethnicity", "NL");
            evidence.put("IBD", "yes");
            evidence.put("migraine_aura", "yes");
            evidence.put("pregnancy_diabetes", "yes");
            evidence.put("current_smoker_e_cigarette_years", "12");
            evidence.put("anxiety_disorder", "yes");
            evidence.put("intervention_bmi", "12");
            evidence.put("current_smoker_cigar_years", "12");
            evidence.put("BMI", "1.2");
            evidence.put("depressive_disorder", "yes");
            evidence.put("rheumatoid_arthritis", "yes");
            evidence.put("net_household_income", "4000-4999");
            evidence.put("migraine", "yes");
            evidence.put("intervention_exercise", "12");
            evidence.put("beta_blocking_agents", "yes");
            evidence.put("TC", "1.2");
            evidence.put("ACR", "1.0");
            evidence.put("antithrombotic_agents", "yes");
            evidence.put("current_smoker_pipe_number_per_week", "12");
            evidence.put("HIV", "yes");
            evidence.put("intervention_smoking", "no");
            evidence.put("COPD", "yes");
            evidence.put("atypical_antipsychotic_medicines", "yes");
            evidence.put("birth_date", "2222-02-22");
            evidence.put("step_count", "12");
            evidence.put("current_smoker", "yes");
            evidence.put("menopause_age", "12");
            evidence.put("Cr", "1.2");
            evidence.put("pregnancy_hypertension", "yes");
            evidence.put("address_house_number", "33");
            evidence.put("DBP", "1");
            evidence.put("current_smoker_e_cigarette_number_per_day", "12");
            evidence.put("menopause", "yes");
            evidence.put("cancer_treatment", "yes");
            evidence.put("current_smoker_e_cigarette", "yes");
            evidence.put("calcium_channel_blockers", "yes");
            evidence.put("current_smoker_cigar", "yes");
            evidence.put("spondylitis_ankylosans", "yes");
            evidence.put("erectile_disfunction", "yes");
            evidence.put("pregnancy_preeclampsia", "yes");
            evidence.put("weight", "1");
            evidence.put("date_question_x_completed", "1");
            evidence.put("CHAMPS_MVPA_score", "0");
            evidence.put("gout", "yes");
            evidence.put("current_smoker_other_number_per_day", "12");
            evidence.put("familial_hypercholesterolemia", "yes");
            evidence.put("drugs_used_in_diabetes", "yes");
            evidence.put("intervention_ldl", "12");
            evidence.put("intervention_glucose", "12");
            evidence.put("intervention_diet", "12");
            evidence.put("psychotic_disorder", "yes");
            evidence.put("current_smoker_substance", "cigarette");
            evidence.put("HDL", "12.0");
            evidence.put("current_smoker_other", "yes");
            evidence.put("LDL", "12.0");
            evidence.put("intervention_sbp", "12");
            evidence.put("CHAMPS_MVPA_Q3", "0");
            evidence.put("CHAMPS_MVPA_Q4", "0");
            evidence.put("CHAMPS_MVPA_Q5", "0");
            evidence.put("CHAMPS_MVPA_Q6", "0");
            evidence.put("date_baseline_consult", "2222-02-22");
            evidence.put("CKD", "yes");
            evidence.put("HbA1c", "12.0");
            evidence.put("CHAMPS_MVPA_Q1", "0");
            evidence.put("CHAMPS_MVPA_Q2", "0");
            evidence.put("eGFR", "1.0");
            evidence.put("SBP", "1");
            evidence.put("antihypertensives", "yes");
            evidence.put("CHAMPS_MVPA_Q7", "0");
            evidence.put("CHAMPS_MVPA_Q8", "0");
            evidence.put("CHAMPS_MVPA_Q9", "0");
            evidence.put("currently_pregnant", "yes");
            evidence.put("current_smoker_cigarette_years", "12");
            evidence.put("current_smoker_cigarette_number_per_day", "12");
            evidence.put("psychiatric_disorder_other", "yes");
            evidence.put("cancer", "yes");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            //just check it doesn't throw an error
            ReducedRiskResponse response = (ReducedRiskResponse) server.estimateBaseLineRisk(req);
        }
    }

    @Test
    public void testClassifyTestAllTheAttributesNoInterventionCorrect()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("date_baseline_consult", "2222-02-22");
            evidence.put("gender", "male");
            evidence.put("birth_date", "2222-02-22");
            evidence.put("ACR", "1.0");
            evidence.put("address_house_number", "33");
            evidence.put("address_postcode", "1234AB");
            evidence.put("alcohol", "12");
            evidence.put("antihypertensives", "yes");
            evidence.put("antithrombotic_agents", "yes");
            evidence.put("anxiety_disorder", "yes");
            evidence.put("arthritis_psoriatica", "yes");
            evidence.put("atypical_antipsychotic_medicines", "yes");
            evidence.put("beta_blocking_agents", "yes");
            evidence.put("bipolar_disorder", "yes");
            evidence.put("calcium_channel_blockers", "yes");
            evidence.put("cancer", "yes");
            evidence.put("cancer_treatment", "yes");
            evidence.put("CHAMPS_MVPA_Q1", "0");
            evidence.put("CHAMPS_MVPA_Q2", "0");
            evidence.put("CHAMPS_MVPA_Q3", "0");
            evidence.put("CHAMPS_MVPA_Q4", "0");
            evidence.put("CHAMPS_MVPA_Q5", "0");
            evidence.put("CHAMPS_MVPA_Q6", "0");
            evidence.put("CHAMPS_MVPA_Q7", "0");
            evidence.put("CHAMPS_MVPA_Q8", "0");
            evidence.put("CHAMPS_MVPA_Q9", "0");
            evidence.put("CHAMPS_MVPA_score", "12");
            evidence.put("CKD", "yes");
            evidence.put("COPD", "yes");
            evidence.put("Cr", "1.2");
            evidence.put("current_smoker_substance", "cigarette");
            evidence.put("current_smoker", "yes");
            evidence.put("current_smoker_cigarette", "yes");
            evidence.put("current_smoker_cigarette_years", "12");
            evidence.put("current_smoker_cigarette_number_per_day", "12");
            evidence.put("current_smoker_cigar", "yes");
            evidence.put("current_smoker_cigar_years", "12");
            evidence.put("current_smoker_cigar_number_per_week", "12");
            evidence.put("current_smoker_pipe", "yes");
            evidence.put("current_smoker_pipe_years", "12");
            evidence.put("current_smoker_pipe_number_per_week", "12");
            evidence.put("current_smoker_e_cigarette", "yes");
            evidence.put("current_smoker_e_cigarette_years", "12");
            evidence.put("current_smoker_e_cigarette_number_per_day", "12");
            evidence.put("current_smoker_other", "yes");
            evidence.put("current_smoker_other_years", "12");
            evidence.put("current_smoker_other_number_per_day", "12");
            evidence.put("currently_pregnant", "yes");
            evidence.put("CVD", "yes");
            evidence.put("CVD_family", "yes");
            evidence.put("depressive_disorder", "yes");
            evidence.put("diabetes_other", "yes");
            evidence.put("diabetes_type_1", "yes");
            evidence.put("diabetes_type_2", "yes");
            evidence.put("difficulties_household_income", "very_difficult");
            evidence.put("drugs_used_in_diabetes", "yes");
            evidence.put("education", "level_1");
            evidence.put("eetscore", "12");
            evidence.put("eGFR", "1.0");
            evidence.put("erectile_disfunction", "yes");
            evidence.put("ethnicity", "NL");
            evidence.put("difficulties_household_income", "very_difficult");
            evidence.put("ex_smoker_substance", "cigarette");
            evidence.put("ex_smoker", "yes");
            evidence.put("ex_smoker_cigarette", "yes");
            evidence.put("ex_smoker_cigarette_years", "12");
            evidence.put("ex_smoker_cigarette_number_per_day", "12");
            evidence.put("ex_smoker_cigar", "yes");
            evidence.put("ex_smoker_cigar_years", "12");
            evidence.put("ex_smoker_cigar_number_per_week", "12");
            evidence.put("ex_smoker_pipe", "yes");
            evidence.put("ex_smoker_pipe_years", "12");
            evidence.put("ex_smoker_pipe_number_per_week", "12");
            evidence.put("ex_smoker_e_cigarette", "yes");
            evidence.put("ex_smoker_e_cigarette_years", "12");
            evidence.put("ex_smoker_e_cigarette_number_per_day", "12");
            evidence.put("ex_smoker_other", "yes");
            evidence.put("ex_smoker_other_years", "12");
            evidence.put("ex_smoker_other_number_per_day", "12");
            evidence.put("familial_hypercholesterolemia", "yes");
            evidence.put("Glu", "12.0");
            evidence.put("gout", "yes");
            evidence.put("HbA1c", "12.0");
            evidence.put("HDL", "12.0");
            evidence.put("HIV", "yes");
            evidence.put("household_size", "12");
            evidence.put("IBD", "yes");
            evidence.put("LDL", "12.0");
            evidence.put("lipid_modifying_agents", "yes");
            evidence.put("menopause", "yes");
            evidence.put("menopause_age", "12");
            evidence.put("migraine", "yes");
            evidence.put("migraine_aura", "yes");
            evidence.put("net_household_income", "4000-4999");
            evidence.put("obstructive_sleep_apnoea", "yes");
            evidence.put("PCOS", "yes");
            evidence.put("pregnancy_diabetes", "yes");
            evidence.put("pregnancy_hypertension", "yes");
            evidence.put("pregnancy_preeclampsia", "yes");
            evidence.put("psychiatric_disorder_other", "yes");
            evidence.put("psychotic_disorder", "yes");
            evidence.put("RAS_inhibitors", "yes");
            evidence.put("rheumatoid_arthritis", "yes");
            evidence.put("SLE", "yes");
            evidence.put("spondylitis_ankylosans", "yes");
            evidence.put("step_count", "12");
            evidence.put("steroid_tablets", "yes");
            evidence.put("TC", "1.2");
            evidence.put("trauma_stressor_disorder", "yes");
            evidence.put("BMI", "1.2");
            evidence.put("DBP", "1");
            evidence.put("height", "1");
            evidence.put("SBP", "1");
            evidence.put("weight", "1");
            evidence.put("date_question_x_completed", "1");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            //just check it doesn't throw an error
            RiskResponse response = (RiskResponse) server.estimateBaseLineRisk(req);
        }
    }

    @Test
    public void testClassifyTestAllTheAttributesOfficialChangesCorrect()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("date_baseline_consult", "2222-02-22");
            evidence.put("gender", "male");
            evidence.put("birth_date", "2222-02-22");
            evidence.put("ACR", "1.0");
            evidence.put("address_house_number", "33");
            evidence.put("address_postcode", "1234AB");
            evidence.put("alcohol", "12");
            evidence.put("antihypertensives", "yes");
            evidence.put("antithrombotic_agents", "yes");
            evidence.put("anxiety_disorder", "yes");
            evidence.put("arthritis_psoriatica", "yes");
            evidence.put("atypical_antipsychotic_medicines", "yes");
            evidence.put("beta_blocking_agents", "yes");
            evidence.put("bipolar_disorder", "yes");
            evidence.put("calcium_channel_blockers", "yes");
            evidence.put("cancer", "yes");
            evidence.put("cancer_treatment", "yes");
            evidence.put("CHAMPS_MVPA_Q1", "0");
            evidence.put("CHAMPS_MVPA_Q2", "0");
            evidence.put("CHAMPS_MVPA_Q3", "0");
            evidence.put("CHAMPS_MVPA_Q4", "0");
            evidence.put("CHAMPS_MVPA_Q5", "0");
            evidence.put("CHAMPS_MVPA_Q6", "0");
            evidence.put("CHAMPS_MVPA_Q7", "0");
            evidence.put("CHAMPS_MVPA_Q8", "0");
            evidence.put("CHAMPS_MVPA_Q9", "0");
            evidence.put("CHAMPS_MVPA_score", "12");
            evidence.put("CKD", "yes");
            evidence.put("COPD", "yes");
            evidence.put("Cr", "1.2");
            evidence.put("current_smoker_substance", "cigarette");
            evidence.put("current_smoker", "yes");
            evidence.put("current_smoker_cigarette", "yes");
            evidence.put("current_smoker_cigarette_years", "12");
            evidence.put("current_smoker_cigarette_number_per_day", "12");
            evidence.put("current_smoker_cigar", "yes");
            evidence.put("current_smoker_cigar_years", "12");
            evidence.put("current_smoker_cigar_number_per_week", "12");
            evidence.put("current_smoker_pipe", "yes");
            evidence.put("current_smoker_pipe_years", "12");
            evidence.put("current_smoker_pipe_number_per_week", "12");
            evidence.put("current_smoker_e_cigarette", "yes");
            evidence.put("current_smoker_e_cigarette_years", "12");
            evidence.put("current_smoker_e_cigarette_number_per_day", "12");
            evidence.put("current_smoker_other", "yes");
            evidence.put("current_smoker_other_years", "12");
            evidence.put("current_smoker_other_number_per_day", "12");
            evidence.put("currently_pregnant", "yes");
            evidence.put("CVD", "yes");
            evidence.put("CVD_family", "yes");
            evidence.put("depressive_disorder", "yes");
            evidence.put("diabetes_other", "yes");
            evidence.put("diabetes_type_1", "yes");
            evidence.put("diabetes_type_2", "yes");
            evidence.put("difficulties_household_income", "very_difficult");
            evidence.put("drugs_used_in_diabetes", "yes");
            evidence.put("education", "level_1");
            evidence.put("eetscore", "12");
            evidence.put("eGFR", "1.0");
            evidence.put("erectile_disfunction", "yes");
            evidence.put("ethnicity", "NL");
            evidence.put("difficulties_household_income", "very_difficult");
            evidence.put("ex_smoker_substance", "cigarette");
            evidence.put("ex_smoker", "yes");
            evidence.put("ex_smoker_cigarette", "yes");
            evidence.put("ex_smoker_cigarette_years", "12");
            evidence.put("ex_smoker_cigarette_number_per_day", "12");
            evidence.put("ex_smoker_cigar", "yes");
            evidence.put("ex_smoker_cigar_years", "12");
            evidence.put("ex_smoker_cigar_number_per_week", "12");
            evidence.put("ex_smoker_pipe", "yes");
            evidence.put("ex_smoker_pipe_years", "12");
            evidence.put("ex_smoker_pipe_number_per_week", "12");
            evidence.put("ex_smoker_e_cigarette", "yes");
            evidence.put("ex_smoker_e_cigarette_years", "12");
            evidence.put("ex_smoker_e_cigarette_number_per_day", "12");
            evidence.put("ex_smoker_other", "yes");
            evidence.put("ex_smoker_other_years", "12");
            evidence.put("ex_smoker_other_number_per_day", "12");
            evidence.put("familial_hypercholesterolemia", "yes");
            evidence.put("Glu", "12.0");
            evidence.put("gout", "yes");
            evidence.put("HbA1c", "12.0");
            evidence.put("HDL", "12.0");
            evidence.put("HIV", "yes");
            evidence.put("household_size", "12");
            evidence.put("IBD", "yes");
            evidence.put("LDL", "12.0");
            evidence.put("lipid_modifying_agents", "yes");
            evidence.put("menopause", "yes");
            evidence.put("menopause_age", "12");
            evidence.put("migraine", "yes");
            evidence.put("migraine_aura", "yes");
            evidence.put("net_household_income", "4000-4999");
            evidence.put("obstructive_sleep_apnoea", "yes");
            evidence.put("PCOS", "yes");
            evidence.put("pregnancy_diabetes", "yes");
            evidence.put("pregnancy_hypertension", "yes");
            evidence.put("pregnancy_preeclampsia", "yes");
            evidence.put("psychiatric_disorder_other", "yes");
            evidence.put("psychotic_disorder", "yes");
            evidence.put("RAS_inhibitors", "yes");
            evidence.put("rheumatoid_arthritis", "yes");
            evidence.put("SLE", "yes");
            evidence.put("spondylitis_ankylosans", "yes");
            evidence.put("step_count", "12");
            evidence.put("steroid_tablets", "yes");
            evidence.put("TC", "1.2");
            evidence.put("trauma_stressor_disorder", "yes");
            evidence.put("BMI", "1.2");
            evidence.put("DBP", "1");
            evidence.put("height", "1");
            evidence.put("SBP", "1");
            evidence.put("weight", "1");
            evidence.put("date_question_x_completed", "1");

            Map<String, String> changes = new HashMap<>();
            changes.put("weight", "2");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            //just check it doesn't throw an error
            ReducedRiskResponse response = (ReducedRiskResponse) server.estimateReducedRisk(req);
        }
    }

    @Test
    public void testClassifyTestInterventionCorrect()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("current_smoker", "yes");
            evidence.put("current_smoker_cigarette", "yes");
            evidence.put("current_smoker_cigarette_years", "12");
            evidence.put("current_smoker_cigarette_number_per_day", "12");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");

            evidence.put("intervention_smoking", "yes");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            ReducedRiskResponse response = (ReducedRiskResponse) server.estimateBaseLineRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : response.getChanges().getChanged().keySet()) {
                name += s + " " + response.getChanges().getChanged().get(s);
            }
            comparisons.put(name, response.getChanges().getProbabilities().get("CVD"));

            assertEquals(comparisons.get("ex_smoker yespack_years 144current_smoker no"), 0.08, 0.01);
        }
    }

    @Test
    public void testClassifyTestPackYearsConversion()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();

            evidence.put("current_smoker", "yes");
            evidence.put("current_smoker_substance", "cigarette");
            evidence.put("current_smoker_cigarette_years", "1");
            evidence.put("current_smoker_cigarette_number_per_day", "2");
            evidence.put("current_smoker_cigarette", "yes");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            RiskResponse response = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response.getProbabilities().size(), 1);

            Map<String, String> evidence2 = new HashMap<>();
            evidence2.put("current_smoker", "yes");
            evidence2.put("current_smoker_substance", "cigar");
            evidence2.put("current_smoker_cigar_years", "2");
            evidence2.put("current_smoker_cigar_number_per_week", "20");
            evidence2.put("current_smoker_cigarette", "no");
            evidence2.put("current_smoker_cigar", "yes");
            evidence2.put("current_smoker_pipe", "no");
            evidence2.put("current_smoker_e_cigarette", "no");
            evidence2.put("current_smoker_other", "no");

            req.setInput(evidence2);

            RiskResponse response2 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response2.getProbabilities().size(), 1);

            Map<String, String> evidence3 = new HashMap<>();
            evidence3.put("current_smoker", "yes");
            evidence3.put("current_smoker_substance", "pipe");
            evidence3.put("current_smoker_pipe_years", "3");
            evidence3.put("current_smoker_pipe_number_per_week", "30");
            evidence3.put("current_smoker_cigarette", "no");
            evidence3.put("current_smoker_cigar", "no");
            evidence3.put("current_smoker_pipe", "yes");
            evidence3.put("current_smoker_e_cigarette", "no");
            evidence3.put("current_smoker_other", "no");

            req.setInput(evidence3);

            RiskResponse response3 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response3.getProbabilities().size(), 1);

            Map<String, String> evidence4 = new HashMap<>();
            evidence4.put("current_smoker", "yes");
            evidence4.put("current_smoker_substance", "e-cigarette");
            evidence4.put("current_smoker_e_cigarette_years", "4");
            evidence4.put("current_smoker_e_cigarette_number_per_day", "40");
            evidence4.put("current_smoker_cigarette", "no");
            evidence4.put("current_smoker_cigar", "no");
            evidence4.put("current_smoker_pipe", "no");
            evidence4.put("current_smoker_e_cigarette", "yes");
            evidence4.put("current_smoker_other", "no");

            req.setInput(evidence4);

            RiskResponse response4 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response4.getProbabilities().size(), 1);

            Map<String, String> evidence5 = new HashMap<>();

            evidence5.put("current_smoker", "no");
            evidence5.put("ex_smoker", "yes");
            evidence5.put("ex_smoker_substance", "cigarette");
            evidence5.put("ex_smoker_cigarette_years", "1");
            evidence5.put("ex_smoker_cigarette_number_per_day", "2");
            evidence5.put("ex_smoker_cigarette", "yes");
            evidence5.put("ex_smoker_cigar", "no");
            evidence5.put("ex_smoker_pipe", "no");
            evidence5.put("ex_smoker_e_cigarette", "no");
            evidence5.put("ex_smoker_other", "no");

            req.setInput(evidence5);

            RiskResponse response5 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response5.getProbabilities().size(), 1);

            Map<String, String> evidence6 = new HashMap<>();
            evidence6.put("current_smoker", "no");
            evidence6.put("ex_smoker", "yes");
            evidence6.put("ex_smoker_substance", "cigar");
            evidence6.put("ex_smoker_cigar_years", "6");
            evidence6.put("ex_smoker_cigar_number_per_week", "60");
            evidence6.put("ex_smoker_cigarette", "no");
            evidence6.put("ex_smoker_cigar", "yes");
            evidence6.put("ex_smoker_pipe", "no");
            evidence6.put("ex_smoker_e_cigarette", "no");
            evidence6.put("ex_smoker_other", "no");

            req.setInput(evidence6);

            RiskResponse response6 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response6.getProbabilities().size(), 1);

            Map<String, String> evidence7 = new HashMap<>();
            evidence7.put("current_smoker", "no");
            evidence7.put("ex_smoker", "yes");
            evidence7.put("ex_smoker_substance", "pipe");
            evidence7.put("ex_smoker_pipe_years", "7");
            evidence7.put("ex_smoker_pipe_number_per_week", "70");
            evidence7.put("ex_smoker_cigarette", "no");
            evidence7.put("ex_smoker_cigar", "no");
            evidence7.put("ex_smoker_pipe", "yes");
            evidence7.put("ex_smoker_e_cigarette", "no");
            evidence7.put("ex_smoker_other", "no");

            req.setInput(evidence7);

            RiskResponse response7 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response7.getProbabilities().size(), 1);

            Map<String, String> evidence8 = new HashMap<>();
            evidence8.put("current_smoker", "no");
            evidence8.put("ex_smoker", "yes");
            evidence8.put("ex_smoker_substance", "e-cigarette");
            evidence8.put("ex_smoker_e_cigarette_years", "8");
            evidence8.put("ex_smoker_e_cigarette_number_per_day", "80");
            evidence8.put("ex_smoker_cigarette", "no");
            evidence8.put("ex_smoker_cigar", "no");
            evidence8.put("ex_smoker_pipe", "no");
            evidence8.put("ex_smoker_e_cigarette", "yes");
            evidence8.put("ex_smoker_other", "no");

            req.setInput(evidence8);

            RiskResponse response8 = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(response8.getProbabilities().size(), 1);


            assertEquals(response.getProbabilities().get("CVD"), 0.085, 0.001);
            assertEquals(response2.getProbabilities().get("CVD"), 0.085, 0.001);
            assertEquals(response3.getProbabilities().get("CVD"), 0.085, 0.001);
            assertEquals(response4.getProbabilities().get("CVD"), 0.087, 0.001);
            assertEquals(response5.getProbabilities().get("CVD"), 0.085, 0.001);
            assertEquals(response6.getProbabilities().get("CVD"), 0.087, 0.001);
            assertEquals(response7.getProbabilities().get("CVD"), 0.087, 0.001);
            assertEquals(response8.getProbabilities().get("CVD"), 0.087, 0.001);
        }
    }

    @Test
    public void testClassifyTestPackYearsInvalidFormat()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();

            evidence.put("current_smoker", "yes");
            evidence.put("current_smoker_substance", "cigarette");
            evidence.put("current_smoker_cigarette", "yes");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");


            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            ExceptionResponse r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Missing attribute 'current_smoker_cigarette_years' is expected to be present");

            evidence.put("current_smoker_cigarette_years", "fout");
            evidence.put("current_smoker_cigarette_number_per_day", "2");
            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Attribute 'current_smoker_cigarette_years' is expected to be an integer value");

        }
    }

    @Test
    public void testClassifyTestWrongNodeName()
            throws Exception {
        {

            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("nonsense", "current_smoker");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            ExceptionResponse r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(), "Unknown attribute 'nonsense'");

        }
    }

    @Test
    public void testClassifyTestWrongState()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("smoking_status", "nonsense");

            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            ExceptionResponse r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Unknown state 'nonsense' for attribute 'smoking_status', expected valid states: " +
                                 "'never_smoker', 'ex_smoker', 'current_smoker'");
        }
    }

    @Test
    public void testScore2Classifier()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("gender", "male");
            evidence.put("age", "50");
            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "140");
            evidence.put("TC", "6.3");
            evidence.put("HDL", "1.4");
            evidence.put("current_smoker_cigarette", "no");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");

            RiskRequest req = new RiskRequest();
            req.setInput(evidence);
            req.setModelType(RiskRequest.ModelType.score2);

            RiskResponse r = (RiskResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getProbabilities().get("CVD"), 0.0631, 0.001);
        }
    }

    @Test
    public void testScore2ClassifierInvalidValues()
            throws Exception {
        {
            String path = "resources/";
            String model = "model.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);


            Map<String, String> evidence = new HashMap<>();
            evidence.put("gender", "nonsense");
            evidence.put("age", "50");
            evidence.put("current_smoker", "no");
            evidence.put("SBP", "140");
            evidence.put("TC", "6.3");
            evidence.put("HDL", "1.4");

            RiskRequest req = new RiskRequest();
            req.setInput(evidence);
            req.setModelType(RiskRequest.ModelType.score2);

            ExceptionResponse r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Unknown state 'nonsense' for attribute 'gender', expected valid states: 'male', 'female'");

            evidence.put("gender", "male");
            evidence.put("current_smoker", "nonsense");

            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(),
                         "Unknown state 'nonsense' for attribute 'current_smoker', expected valid states: 'yes', 'no'");

            evidence.put("current_smoker", "yes");
            evidence.put("SBP", "nonsense");
            evidence.put("current_smoker_cigarette", "no");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");

            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(), "Attribute 'SBP' is expected to be an double value");

            evidence.remove("SBP");
            //readd smoking variables due to unit-test passing along values, unlike the real world where i would be a
            // new list
            evidence.put("current_smoker_cigarette", "no");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");

            r = (ExceptionResponse) server.estimateBaseLineRisk(req);
            assertEquals(r.getMessage(), "Missing attribute 'SBP' is expected to be present");
        }
    }

    @Test
    public void testComparison()
            throws Exception {
        {
            String path = "resources/";
            String model = "dummy_model_sananet.pgmx";

            Server server = new Server("CVD", "yes", RiskRequest.ModelType.bayesian, path, model);

            Map<String, String> evidence = new HashMap<>();
            evidence.put("current_smoker", "yes");
            evidence.put("current_smoker_cigarette", "no");
            evidence.put("current_smoker_cigar", "no");
            evidence.put("current_smoker_pipe", "no");
            evidence.put("current_smoker_e_cigarette", "no");
            evidence.put("current_smoker_other", "no");


            ReducedRiskRequest req = new ReducedRiskRequest();
            req.setInput(evidence);

            Map<String, String> change1 = new HashMap<>();
            change1.put("current_smoker", "no");

            req.setChanges(change1);

            ReducedRiskResponse result = (ReducedRiskResponse) server.estimateReducedRisk(req);

            Map<String, Double> comparisons = new HashMap<>();

            String name = "";
            for (String s : result.getChanges().getChanged().keySet()) {
                name += s + " " + result.getChanges().getChanged().get(s);
            }
            comparisons.put(name, result.getChanges().getProbabilities().get("CVD"));


            assertEquals(comparisons.get("pack_years 0current_smoker no"), 0.085, 0.01);
        }
    }

    private String expectedModel(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, "UTF-8");
    }
}