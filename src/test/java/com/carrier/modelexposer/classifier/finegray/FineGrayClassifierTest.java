package com.carrier.modelexposer.classifier.finegray;

import com.carrier.modelexposer.webservice.domain.ReducedRiskResponse;
import com.carrier.modelexposer.webservice.domain.RiskResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FineGrayClassifierTest {
    @Test
    public void testClassifyTestSanaNetExample()
            throws Exception {
        {
            Map<String, String> evidence = new HashMap<>();
            evidence.put("age", "65");
            evidence.put("gender", "female");
            //postal code and adress will be transformed into seswoa.
            //This is handled by the Server. For this testcase it is done manually.
//            evidence.put("adress_postcode", "8017AV");
//            evidence.put("adress_house_number", "67");

            evidence.put("seswoa", "0.375");
            evidence.put("current_smoker", "yes");
            evidence.put("ex_smoker", "null");
            evidence.put("CHAMPS_MVPA_score", "3");
            evidence.put("eetscore", "80");
            evidence.put("SBP", "107");
            evidence.put("TC", "7.1");
            evidence.put("HDL", "1");
            evidence.put("LDL", "null");
            evidence.put("antihypertensives", "null");
            evidence.put("beta_blocking_agents", "null");
            evidence.put("calcium_channel_blockers", "null");
            evidence.put("RAS_inhibitors", "null");
            evidence.put("lipid_modifying_agents", "no");

            FineGrayClassifier classifier = new FineGrayClassifier();
            RiskResponse response = classifier.classify(evidence);
            assertEquals(response.getProbabilities().get("CVD"), 0.0748, 0.0001);

            Map<String, String> intervention = new HashMap<>();
            intervention.put("age", "65");
            intervention.put("gender", "female");
            //postal code and adress will be transformed into seswoa.
            //This is handled by the Server. For this testcase it is done manually.
//            intervention.put("adress_postcode", "8017AV");
//            intervention.put("adress_house_number", "67");

            intervention.put("seswoa", "0.375");

            intervention.put("intervention_smoking", "no");
            //manually put in intervention since this doesn't go via server
            intervention.put("current_smoker", "no");
            intervention.put("ex_smoker", "yes");
            intervention.put("CHAMPS_MVPA_score", "3");
            intervention.put("eetscore", "80");
            intervention.put("SBP", "107");
            intervention.put("TC", "7.1");
            intervention.put("HDL", "1");
            intervention.put("LDL", "null");
            intervention.put("antihypertensives", "null");
            intervention.put("beta_blocking_agents", "null");
            intervention.put("calcium_channel_blockers", "null");
            intervention.put("RAS_inhibitors", "null");
            intervention.put("lipid_modifying_agents", "no");
            intervention.put("intervention_exercise", "null");
            intervention.put("intervention_diet", "null");
            intervention.put("intervention_sbp", "null");
            intervention.put("intervention_ldl", "null");

            ReducedRiskResponse reduced = classifier.compareClassifications(evidence, intervention);
            assertEquals(reduced.getBaseline().getProbabilities().get("CVD"), 0.0748, 0.0001);
            assertEquals(reduced.getChanges().getProbabilities().get("CVD"), 0.04236, 0.0001);
        }
    }
}