# ModelExposer

Exposes a model via a JSON rest API.

## Docker hub:
Docker images can be found at: https://hub.docker.com/repository/docker/fvandaalen/carrier

### Included models:

Currently it can only run bayesian models It uses the openmarkov library ( http://www.openmarkov.org/ )
A current jar is included in `./openmarkov/OpenMarkov-0.4.0.jar`. Use this as the library due to their maven repo being
down To install the jar locally in your mvn repo use the following command:

`mvn install:install-file -Dfile=./openmarkov/OpenMarkov-0.4.0.jar -DgroupId=org.openmarkov -DartifactId=org.openmarkov.full -Dversion=0.4.0-SNAPSHOT -Dpackaging=jar`

### Storing models:

The spring configuration expects the following:
`modelpath=C:/playground/modelExposer/ model=BN-two-diseases.pgmx`
This contains the path to the model and the name of the bayesian model.

The model is expected to come as a .pgmx file.


### Classifying an individual

The rest endpoint /classifyIndividualBayesian is available for this.

It expects a JSON post request that looks as follows:

```
{
    "evidence" : {
        "Symptom" : "absent"
    },
    "targets" : [ "Disease 1", "Disease 2" ]
}
```

Evidence corresponds to the known values belonging to the individual you want to classify. Targets contains the lists of
attributes you would like to predict/classify.

It will then return a response that has the following form:

```{
  "attributes": [
    {
      "probabilities": {
        "absent": 0.9987185485019221,
        "present": 0.001281451498077823
      },
      "name": "Disease 2"
    },
    {
      "probabilities": {
        "absent": 0.9988980898581895,
        "present": 0.0011019101418104733
      },
      "name": "Disease 1"
    }
  ]
}```