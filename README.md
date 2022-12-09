## Available models:

- Bayesian network

## Models in progress:

- Cox proportional hazard
- Neural networks
- Bayesian ensemble

- - -

## System requirements:

This docker image has been tested on a system with the following:

- Processor: Intel(R) Core(TM) i7-10750H CPU @ 2.60GHz 2.59 GHz
- RAM: 16GB
- A working local docker installation

### Image size:

- Currently with only the bayesian network: 385MB

## Running the models:

### Bayesian network:

A docker container can be found at fvandaalen/carrier on dockerhub.

Execute: `docker run -p 8080:8080 fvandaalen/carrier:modelexposer_bayesian`
This allows for the following webservice call:

- http://localhost:8080/estimateBaseLineRisk
- http://localhost:8080/estimateReducedRisk

The first call estimates the risk for CVD for a given individual, the second also provides a number of comparisons with
certain baseline values. These comparisons are very basic, only changing 1 attribute at a time. They also do note take
into account if the original patient already did better than this baseline (e.g. it will calculate the probability of
CSV if the patient stops smoking for a patient who never smoked to begin with)

#### Input:

Both methods expect the same input as the basis: values of all known input variables, as well as the target variables of
interest in JSON format. It can deal with missing input variables. Expects a POST request. If modelType is left empty
the modelExposer defaults to the Bayesian Network

```
{
  "input" : {
    "smoking_status" : "current_smoker"
  },
  "modelType" : "bayesian"
}
```

In addition to this it is possible to indicate the comparisons of interest for the estimateReducedRisk endpoint, as
opposed to using the basic comparisons provided by the modelExposer. Input with comparisons looks like this:

```
{
  "input" : {
    "smoking_status" : "current_smoker"
  },
  "comparisons" : [ {
    "smoking_status" : "ex_smoker",
    "nutrition_score" : "medium"
  }, {
    "nutrition_score" : "medium"
  }, {
    "nutrition_score" : "high"
  }, {
    "physical_activity_score" : "medium"
  }, {
    "physical_activity_score" : "high"
  } ],
  "modelType" : "bayesian"
}

Process finished with exit code 0

```

#### Output:

probabilities of the various possible values for the target variable in JSON format. Both endpoints return similar
output:
The main difference is that the estimateBaseLineRisk-endpoint returns a JSON object containing 1 list of attributes with
probabilities. The estimateReducedRisk-endpoint however returns a JSON object that contains 1 list, labeled "baseline",
for the original data and a list of comparisons, labeled "comparisons".

estimateBaseLineRisk-endpoint:

```
{
  "probabilities": {
    "CVD": 0.07865005183850245
  }
}
```

estimateReducedRisk-endpoint:

```
{
   "comparisons":    [
            {
         "probabilities": {"CVD": 0.04145569771244592},
         "changed": {"smoking_status": "ex_smoker"}
      },
            {
         "probabilities": {"CVD": 0.0733798604187438},
         "changed": {"nutrition_score": "medium"}
      },
            {
         "probabilities": {"CVD": 0.05225324027916252},
         "changed": {"nutrition_score": "high"}
      },
            {
         "probabilities": {"CVD": 0.07483549351944169},
         "changed": {"physical_activity_score": "medium"}
      },
            {
         "probabilities": {"CVD": 0.07483549351944169},
         "changed": {"physical_activity_score": "high"}
      }
   ],
   "baseline": {"probabilities": {"CVD": 0.07865005183850245}}
}
```

#### Model:

The model is still under construction right now a dummy model is contained in the image

### Error handling:

Two types of user errors will result in a response to the user indicating something went wrong. These two errors will
return a JSON object that only contains the error message, indicating what went wrong.

The JSON object looks as follows:

````
{"message":"error"}
````

The following two exceptions can be thrown:

- UnknownAttribute Exception:
    - message: "Unknown attribute 'x'"
- UnknownState Exception:
    - message: "Unknown state 'x' for attribute 'y',expected valid states: 'a', 'b', 'c'"

- - -

### Example calls that can currenty be used for reference in a unit-test:

- - - 

#### estimateBaseLineRisk-endpoint:

Example 1:
input:

```
{
  "input" : {
    "age" : "1"
  }
}
```

output:

```
{"probabilities": {"CVD": 0.08650000000000001}}
```

--- 
Example 2:
input:

```
{
  "input" : {
    "current_smoker" : "yes"
  }
}
```

output:

```
{"probabilities": {"CVD": 0.08650000000000002}}
```

--- 
Example 3:
input:

```
{
  "input" : {
    "current_smoker" : "yes",
    "gender" : "male"
  }
}
```

output:

```
{"probabilities": {"CVD": 0.0865}}
```

--- 
Example 4:
input:

```
{
  "input" : {
    "current_smoker" : "nonsense",
    "gender" : "male"
  }
}
```

output:

```
{"message": "Unknown state 'nonsense' for attribute 'current_smoker', expected valid states: 'no', 'yes'"}
```

--- 
Example 5:
input:

```
{
  "input" : {
    "nonsense" : "nonsense",
    "gender" : "male"
  }
}
```

output:

```
{"message": "Unknown attribute 'nonsense'"}
```

--- 

#### estimateReducedRisk-endpoint

estimateReducedRisk does not currently work while the model is still under construction due to the need to hardcode the
comparisons.