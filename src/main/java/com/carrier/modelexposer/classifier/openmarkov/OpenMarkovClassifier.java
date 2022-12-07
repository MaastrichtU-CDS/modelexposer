package com.carrier.modelexposer.classifier.openmarkov;


import com.carrier.modelexposer.classifier.Classifier;
import com.carrier.modelexposer.exception.UnknownAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
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

    public ReducedRiskResponse compareClassifications(Map<String, String> evidence,
                                                      List<Map<String, String>> comparisons)
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException, UnknownStateException,
                   UnknownAttributeException {

        ReducedRiskResponse result = new ReducedRiskResponse();
        result.setBaseline(classify(evidence));
        for (Map<String, String> comparison : comparisons) {
            Map<String, String> evidences = new HashMap<>();
            evidences.putAll(evidence);
            evidences.putAll(comparison);
            result.addResult(comparison, classify(evidences).getProbabilities());
        }
        return result;
    }

    public RiskResponse classify(Map<String, String> evidence)
            throws UnknownAttributeException, UnknownStateException, IncompatibleEvidenceException,
                   InvalidStateException, NotEvaluableNetworkException {

        VEPropagation vePropagation;
        EvidenceCase postResolutionEvidence = new EvidenceCase();
        EvidenceCase preResolutionEvidence = null;

        List<Variable> variablesOfInterest = new ArrayList<>();
        for (String target : target.keySet()) {
            try {
                variablesOfInterest.add(network.getVariable(target));
            } catch (NodeNotFoundException e) {
                throw new UnknownAttributeException(target);
            }
        }

        for (String key : evidence.keySet()) {
            Variable v = null;
            try {
                v = network.getVariable(key);
            } catch (NodeNotFoundException e) {
                throw new UnknownAttributeException(key);
            }
            if (v.getVariableType() == VariableType.FINITE_STATES) {
                int index = 0;
                try {
                    index = v.getStateIndex(v.getState(evidence.get(key)));
                } catch (InvalidStateException e) {
                    String validStates = "";
                    for (int i = 0; i < v.getNumStates(); i++) {
                        if (validStates.length() > 0) {
                            validStates += ", ";
                        }
                        validStates += "'" + v.getStateName(i) + "'";
                    }
                    throw new UnknownStateException(evidence.get(key), key, validStates);
                }
                Finding f = new Finding(v, index);
                postResolutionEvidence.addFinding(f);
            } else if (v.getVariableType() == VariableType.DISCRETIZED) {
                Finding f = new Finding(v, Double.valueOf(evidence.get(key)));
                postResolutionEvidence.addFinding(f);
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

