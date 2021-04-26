# ProbabilityCollection

A Probability Collection is a representation of a weighted pool.

Example:
* `a` has a 1/5 (20%) chance, `b` and `c` both have a 2/5 (40%) chance.
  ```yml
  - a: 1
  - b: 2
  - c: 2 
  ```

# Weighted Pool

A weighted pool is a way to randomly select one of many objects from a "pool" based on "weight".

## Pools
A pool is a list of objects from which selections will be pulled.

## Weight
Weight is the *relative* probability that any object will be selected from the pool. Each object in the pool has a
weight, meaning the chance of object O being selected from pool P is `weight(O)/sum(weight(P_n))`.

## Example
Here's an example pool:
```yaml
OPTION_A: 2
OPTION_B: 1
OPTION_C: 1
```
In this YAML-based pool, the keys are the objects, and the values are the weights. If we want the probability that
`OPTION_A` will be selected, we sum all weights, and divide the weight of `OPTION_A` by that sum.  
`2 + 1 + 1 = 4`   
Therefore the probability that OPTION_A will be selected is `2/4`, or 50%.

## Manipulating weights to get a desired probability
What if we want Option A to have a 60% chance of being selected? This requires manipulating the weights of all objects
in the pool, since with B and C having a weight of 1, there is no integer weight that can be assigned to A that will
give it a probability of 60%. What is usually done is that the weights are given a set sum that is easy to work with.
Usually this is 100, since with a sum of 100, every object's weight is equal to the percent chance it will be selected.
By applying this to the above example, we can produce this pool that has a 60% chance of A being selected:
```yaml
OPTION_A: 60
OPTION_B: 20
OPTION_C: 20
```

## Use Cases
Sometimes, it is undesirable to have an equal probability of all objects being selected, so you may want to "weight"
some objects higher, so they are selected more often.
