package com.srh.api.algorithms.resources.utils;

import com.srh.api.algorithms.resources.basedcontent.EvaluatorProfileMatrix;
import com.srh.api.model.*;
import com.srh.api.service.ItemRatingService;
import com.srh.api.service.ProjectService;
import com.srh.api.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EvaluatorTagBaseMatrix extends BaseMatrix {
    // USADA PARA A TRANSIÇÃO ENTRE CONTEUDO E COLABORATIVA NA FILTRAGEM CASCADE/COMBINAÇÃO SEQUENCIAL.
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ItemRatingService itemRatingService;

    @Autowired
    private TagService tagService;

    public void build(Integer projectId) {
        project = projectService.find(projectId);
        evaluators = project.getEvaluators();
        tags = tagService.findAll();

        rowSize = evaluators.size();
        colSize = tags.size();
        
        content = new Double[rowSize][colSize];

        //generateMatrix();
    }

    public void addProfiles(Evaluator evaluator, EvaluatorProfileMatrix evaluatorProfileMatrix){
        // Copia o avaragerow (o perfil em si) para o msm row que está o evaluator
        for (int i = 0; i< colSize; i++){
            content[evaluatorProfileMatrix.getEvaluatorRow()][i] 
                = evaluatorProfileMatrix.getAvarageRow()[i];
        }
    }

    public void dumpContentMatrix(){
        for (int i=0; i < rowSize; i++){
            for (int j=0; j < colSize; j++){
                System.out.print(content[i][j]+",");
            }
            System.out.println("");
        }
        //Testing purposes only
    }
}

