// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2019
// diego.catalano at live.com
//
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Evolutionary.Metaheuristics;

import Catalano.Core.DoubleRange;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Salp Swarm Algorithm (SSA). Need to finish this algorithm.
 * 
 * Need a describe.
 * 
 * References:
 * Mirjalili, Seyedali, et al.
 * "Salp Swarm Algorithm: A bio-inspired optimizer for engineering design problems." Advances in Engineering Software 114 (2017): 163-191.
 * 
 * @author Diego Catalano
 */
public class SalpSwarmAlgorithm extends BaseEvolutionaryOptimization{

    /**
     * Initializes a new instance of the SalpSwarmAlgorithm class.
     */
    public SalpSwarmAlgorithm() {
        this(25,1000);
    }
    
    /**
     * Initializes a new instance of the SalpSwarmAlgorithm class.
     * @param population Population size.
     * @param generations Generations.
     */
    public SalpSwarmAlgorithm(int population, int generations){
        this.populationSize = population;
        this.generations = generations;
    }

    @Override
    public void Compute(IObjectiveFunction function, List<DoubleRange> boundConstraints) {
        
        Random rand = new Random();

        //Reset variables
        minError = Double.MAX_VALUE;
        nEvals = 0;
        
        //Create the population
        List<Individual> pop = Individual.CreatePopulation(populationSize, boundConstraints, function);
        Collections.sort(pop);
        
        minError = pop.get(0).getFitness();
        best = Arrays.copyOf(pop.get(0).getLocation(), boundConstraints.size());
        
        //Main algorithm
        for (int g = 0; g < generations; g++) {
            
            double c1 = 2 * Math.exp(-Math.pow(4*(g+1)/generations,2));
            
            for (int i = 0; i < pop.size(); i++) {
                
                if(i <= pop.size() / 2){
                    for (int j = 0; j < boundConstraints.size(); j++) {
                        DoubleRange range = boundConstraints.get(j);
                        if(rand.nextDouble() < 0.5)
                            pop.get(i).getLocation()[j] = best[j] + c1 * (range.getMax() - range.getMin()) * rand.nextDouble() + range.getMin();
                        else
                            pop.get(i).getLocation()[j] = best[j] - c1 * (range.getMax() - range.getMin()) * rand.nextDouble() + range.getMin();
                    }
                }
                else{
                    //TODO
                }
            }
            
            //Calculate fitness
            for (int i = 0; i < pop.size(); i++) {
                double f = function.Compute(pop.get(i).getLocation());
                pop.get(i).setFitness(f);
                if(f < minError){
                    minError = f;
                    best = Arrays.copyOf(pop.get(i).getLocation(), boundConstraints.size());
                }
            }
            
            //Update on listener
            if(listener != null) listener.onIteration(g+1, minError);
            
        }
        
    }
    
}