package com.carrier.modelexposer.webservice.openmarkov;


import com.carrier.modelexposer.webservice.domain.Attribute;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualResponse;
import org.openmarkov.core.exception.*;
import org.openmarkov.core.model.network.*;
import org.openmarkov.core.model.network.potential.TablePotential;
import org.openmarkov.inference.variableElimination.tasks.VEPropagation;
import org.openmarkov.io.probmodel.reader.PGMXReader_0_2;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenMarkovClassifier {
    @Value ("${path}")
    String path;
    String model;

    private ProbNet network;

    public OpenMarkovClassifier(String path, String model) {
        this.path = path;
        this.model = model;
        loadModel();
    }

    public OpenMarkovClassifier() {
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

    public ClassifyIndividualResponse classify(Map<String, String> evidence, List<String> targets)
            throws NodeNotFoundException, IncompatibleEvidenceException, InvalidStateException,
                   NotEvaluableNetworkException, UnexpectedInferenceException {

        VEPropagation vePropagation;
        EvidenceCase postResolutionEvidence = new EvidenceCase();
        EvidenceCase preResolutionEvidence = null;

        List<Variable> variablesOfInterest = new ArrayList<>();
        for (String target : targets) {
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

    private ClassifyIndividualResponse createResults(HashMap<Variable, TablePotential> posteriorValues) {
        List<Attribute> attributes = new ArrayList<>();
        for (Variable key : posteriorValues.keySet()) {
            Attribute a = new Attribute();
            Map<String, Double> prob = new HashMap<>();
            a.setProbabilities(prob);
            TablePotential potential = posteriorValues.get(key);
            for (int i = 0; i < potential.values.length; i++) {
                prob.put(key.getStateName(i), potential.values[i]);
            }
            attributes.add(a);
            a.setName(key.getName());
        }
        ClassifyIndividualResponse response = new ClassifyIndividualResponse();
        response.setAttributes(attributes);
        return response;
    }
}

