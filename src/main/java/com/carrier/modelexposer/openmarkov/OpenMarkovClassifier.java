package com.carrier.modelexposer.openmarkov;


import com.carrier.modelexposer.classifier.Classifier;
import com.carrier.modelexposer.webservice.domain.ReducedRiskResponse;
import com.carrier.modelexposer.webservice.domain.RiskResponse;
import org.apache.commons.io.IOUtils;
import org.openmarkov.core.exception.*;
import org.openmarkov.core.model.network.*;
import org.openmarkov.core.model.network.potential.TablePotential;
import org.openmarkov.inference.variableElimination.tasks.VEPropagation;
import org.openmarkov.io.probmodel.reader.PGMXReader_0_2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.carrier.modelexposer.baseline.BaseLine.collectExampleBaseLinesEvidences;

public class OpenMarkovClassifier extends Classifier {
    private String path;
    private String model;
    private final Map<String, String> target;

    private ProbNet network;

    public OpenMarkovClassifier(String path, String model, String target, String targetValue) {
        this.path = path;
        this.model = model;
        this.target = new HashMap<>();
        this.target.put(target, targetValue);
        loadModel();
    }

    public void loadModel() {
        try {
            // Open the file containing the network
            InputStream file = new FileInputStream(path + model);

            // Load the Bayesian network
            PGMXReader_0_2 pgmxReader = new PGMXReader_0_2();
            network = pgmxReader.loadProbNet(model, file);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public ReducedRiskResponse compareClassifications(Map<String, String> evidence)
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        List<Map<String, String>> baselineEvidences = collectExampleBaseLinesEvidences();
        ReducedRiskResponse result = new ReducedRiskResponse();
        result.setBaseline(classify(evidence));
        for (Map<String, String> baseline : baselineEvidences) {
            Map<String, String> evidences = new HashMap<>();
            evidences.putAll(evidence);
            evidences.putAll(baseline);
            result.addResult(baseline, classify(evidences).getProbabilities());
        }
        return result;
    }

    public RiskResponse classify(Map<String, String> evidence)
            throws NodeNotFoundException, IncompatibleEvidenceException, InvalidStateException,
                   NotEvaluableNetworkException, UnexpectedInferenceException {

        VEPropagation vePropagation;
        EvidenceCase postResolutionEvidence = new EvidenceCase();
        EvidenceCase preResolutionEvidence = null;

        List<Variable> variablesOfInterest = new ArrayList<>();
        for (String target : target.keySet()) {
            variablesOfInterest.add(network.getVariable(target));
        }

        for (String key : evidence.keySet()) {
            Variable v = network.getVariable(key);
            if (v.getVariableType() == VariableType.FINITE_STATES) {
                int index = v.getStateIndex(v.getState(evidence.get(key)));
                Finding f = new Finding(v, index);
                postResolutionEvidence.addFinding(f);
            } else if (v.getVariableType() == VariableType.DISCRETIZED) {
                Finding f = new Finding(v, Double.valueOf(evidence.get(key)));
            }
        }


        vePropagation = new VEPropagation(network);
        vePropagation.setVariablesOfInterest(variablesOfInterest);
        vePropagation.setPreResolutionEvidence(preResolutionEvidence);
        vePropagation.setPostResolutionEvidence(postResolutionEvidence);
        HashMap<Variable, TablePotential> posteriorVales = vePropagation.getPosteriorValues();

        return createResults(posteriorVales);
    }

    private RiskResponse createResults(HashMap<Variable, TablePotential> posteriorValues) {
        Map<String, Double> probabilities = new HashMap<>();
        for (Variable key : posteriorValues.keySet()) {
            String name = key.getName();
            TablePotential potential = posteriorValues.get(key);
            for (int i = 0; i < potential.values.length; i++) {
                if (key.getStateName(i).equals(target.get(name))) {
                    probabilities.put(name, potential.values[i]);
                }
            }
        }
        RiskResponse response = new RiskResponse();
        response.setProbabilities(probabilities);
        return response;
    }

    public String getModelPgmx() {
        try {
            return readModel(path + model);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ProbNet getNetwork() {
        return network;
    }

    private String readModel(String path)
            throws IOException {

        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, "UTF-8");
    }
}

