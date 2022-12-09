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

The first call estimates the risk for CVD for a given individual, the second also provides a number of changes with
certain baseline values. These changes are very basic, only changing 1 attribute at a time. They also do note take into
account if the original patient already did better than this baseline (e.g. it will calculate the probability of CSV if
the patient stops smoking for a patient who never smoked to begin with)

#### Input estimateBaseLineRisk

If modelType is left empty we will default to the docker image.

```
{
  "input" : {
    "current_smoker" : "yes"
  },
  "modelType" : "bayesian"
}
```

#### Output estimateBaseLineRisk:

```
{
  "probabilities": {
    "CVD": 0.07865005183850245
  }
}
```

#### Input estimateReducedRisk-endpoint
Similar to estimateBaseLineRisk the field modelType is optional
```
{
  "input" : {
    "gender" : "male"
  },"changes":[{
  	"current_smoker":"yes"
  },{
  	"BMI":"20"
  	}],
	"modelType" : "bayesian"
}
```
#### Output estimateReducedRisk-endpoint

```
{
   "changes":    [
            {
         "probabilities": {"CVD": 0.0865},
         "changed": {"current_smoker": "yes"}
      },
            {
         "probabilities": {"CVD": 0.0865},
         "changed": {"BMI": "20"}
      }
   ],
   "baseline": {"probabilities": {"CVD": 0.08650000000000001}}
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

Example 1:
Input:
```
{
  "input" : {
    "gender" : "male"
  },"changes":[{
  	"current_smoker":"yes"
  },{
  	"BMI":"20"
  	}],
	"modelType" : "bayesian"
}
```

Output:
```
{
   "changes":    [
            {
         "probabilities": {"CVD": 0.0865},
         "changed": {"current_smoker": "yes"}
      },
            {
         "probabilities": {"CVD": 0.0865},
         "changed": {"BMI": "20"}
      }
   ],
   "baseline": {"probabilities": {"CVD": 0.08650000000000001}}
}
```
---
Example 2:
Input:
```
{
  "input" : {
    "gender" : "male"
  },"changes":{
  	"current_smoker":"yes",
  	"gout":"yes"
  },
  	"modelType" : "bayesian"
}
```

Output:
```
{
   "changes":    {
      "probabilities": {"CVD": 0.083},
      "changed":       {
         "current_smoker": "yes",
         "gout": "yes"
      }
   },
   "baseline": {"probabilities": {"CVD": 0.08650000000000001}}
}
```