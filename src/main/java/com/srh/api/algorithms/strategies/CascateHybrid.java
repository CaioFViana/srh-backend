package com.srh.api.algorithms.strategies;

import com.srh.api.algorithms.math.CellPosition;
import com.srh.api.algorithms.resources.*;
import com.srh.api.algorithms.resources.basedcontent.SimilarityEvaluatorContent;
import com.srh.api.algorithms.resources.basedcontent.EvaluatorProfileMatrix;
import com.srh.api.algorithms.resources.basedcontent.ItemTagMatrix;
import com.srh.api.algorithms.resources.basedcontent.SimilarityEvaluatorProfile;
import com.srh.api.algorithms.resources.utils.BasicBaseMatrix;
import com.srh.api.algorithms.resources.utils.EvaluatorTagBaseMatrix;
import com.srh.api.algorithms.resources.utils.RecommendationUtils;
import com.srh.api.algorithms.resources.utils.RecommendationsByEvaluator;
import com.srh.api.builder.AlgorithmBuilder;
import com.srh.api.builder.RecommendationBuilder;
import com.srh.api.dto.resource.RecommendationForm;
import com.srh.api.model.Algorithm;
import com.srh.api.model.Evaluator;
import com.srh.api.model.Item;
import com.srh.api.model.Recommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CascateHybrid implements RecommendationAlgorithm {
    @Autowired
    private BasicBaseMatrix primaryMatrix;

    @Autowired
    private EvaluatorTagBaseMatrix evaluatorTagMatrix;

    @Autowired
    private ItemTagMatrix itemTagMatrix;

    @Autowired
    private RecommendationUtils recommendationUtils;

    private final List<RecommendationsByEvaluator> recommendationsByEvaluators = new ArrayList<>();
    private List<CellPosition> recommendationsPositions = new ArrayList<>();
    private Double passingScore;
    private Integer decimalPrecision;
    private LocalDateTime startRecommendation;

    @Override
    public List<RecommendationsByEvaluator> calc(RecommendationForm form) {
        // Passo 1, pegar os dados do projeto e montar matrizes iniciais
        passingScore = form.getPassingScore();
        decimalPrecision = form.getDecimalPrecision();

        //buildBasicMatrix(form.getProjectId());
        primaryMatrix.build(form.getProjectId());
        itemTagMatrix.build(primaryMatrix.getItems());

        evaluatorTagMatrix.build(form.getProjectId());

        // Passo 2, Calcular o perfil de todo mundo e reunir em um lugar só.
        for(Evaluator evaluator: primaryMatrix.getEvaluators()) {
            EvaluatorProfileMatrix evaluatorProfileMatrix = mountEvaluatorProfile(evaluator);
            evaluatorTagMatrix.addProfiles(evaluator, evaluatorProfileMatrix);
            // matriz retornada por evaluatorProfileMatrix aqui encima é o calculo de profile de TODO MUNDO (profile = perfil = média da content.)
            // Contém métodos para pegar a fileira do evaluator e o array do perfil dele!
            // o evaluator-tag Matrix recebe com todo prazer e armazena. 
            
            //RecommendationsByEvaluator recommendationsByEvaluator = calculateRecommendationByEvaluator(
                    //evaluatorProfileMatrix, itemTagMatrix, evaluator);
            //recommendationsByEvaluators.add(recommendationsByEvaluator);
        }
        //DEBUG
        for (int i=0; i < primaryMatrix.getRowSize(); i++){
            for (int j=0; j < primaryMatrix.getColSize(); j++){
                System.out.print(primaryMatrix.getContent()[i][j]+",\t");
            }
            System.out.println("");
        }
        evaluatorTagMatrix.dumpContentMatrix();
        Double[] debug = evaluatorTagMatrix.getSimilarityArray(primaryMatrix.getEvaluators().get(1),decimalPrecision);
        for (int j=0; j < debug.length; j++){
            System.out.print(debug[j]+",\t");
        }
        //Checado e passo 1 e 2 estão ok.
        
        // Passo 3 - Fazer a filtragem colaborativa nisso.
        // Plano: checar o primary matrix por valores vazios. nao existente estão null lá. mas checa por 0 tb.
        //      Feito isso, sabemos o produto E o evaluator que precisa
        //      Pega a similarity row, detecta quem comprou o produto e as notas deles
        //      Segue o slide, e faz o que é feito lá.
        //      Finaliza com o save. comando é
        //RecommendationUtils recUtils = new RecommendationUtils();
        //recUtils.buildRecommendation(score, startRecommendationTime, algorithmId, item, evaluator, project)
        
        for (int i=0; i < primaryMatrix.getRowSize(); i++){
            for (int j=0; j < primaryMatrix.getColSize(); j++){
                if(!isNonZero(primaryMatrix.getContent()[i][j])) {
                    // Precisa estimar nota. trabalha!
                    Evaluator currentEvaluator = primaryMatrix.getEvaluators().get(i);
                    Double[] similarityFromEvaluator = evaluatorTagMatrix.getSimilarityArray(currentEvaluator, decimalPrecision);
                    Double sumOfSimiTimesScore = 0.0;
                    Double sumOfSimiEvaluator  = 0.0;
                    for(int k=0; k<primaryMatrix.getRowSize();k++){
                        // J é constante, pois ele definiu o produto. K vai varrer a coluna para ver quem já comprou ele.
                        if (isNonZero(primaryMatrix.getContent()[k][j])){
                            // Comprou o produto! hora do cálculo
                            sumOfSimiTimesScore += (similarityFromEvaluator[k] * primaryMatrix.getContent()[k][j]);
                            sumOfSimiEvaluator  +=  similarityFromEvaluator[k];
                        }
                    }
                    // Percorrida as notas. hora de soltar a nota prevista para a recomendação:
                    Double score = sumOfSimiTimesScore/ sumOfSimiEvaluator;
                    //DEBUG
                    System.out.println(score + " - " + i + " " + j);
                }
            }
        }
        
        // PRA FECHAR PARTE 3, FALTA CRIAR O VETOR DE RECOMENDATIONS.


        // Passo 4
        // Mas pra voltar pro calc, precisa de ser esse OBJ ai
        //List<Recommendation> recommendations, depois .add() o retorno do utils do build ai.
        // VER calculateRecommendationByEvaluator DA COLABORATIVA!!!! É A CHAVE DO FINALE!



        recommendationUtils.defineNewMatrixId(form.getProjectId());
        return recommendationsByEvaluators;
    }

    private boolean isNonZero(Double value){
        if(value == null || value == 0.0) return false;
        else return true;
    }

    private void buildBasicMatrix(Integer projectId) {
        primaryMatrix.build(projectId);
    }

    private EvaluatorProfileMatrix mountEvaluatorProfile(Evaluator evaluator) {
        EvaluatorProfileMatrix evaluatorProfileMatrix = new EvaluatorProfileMatrix();
        evaluatorProfileMatrix.build(evaluator, primaryMatrix, itemTagMatrix);
        return evaluatorProfileMatrix;
    }

    private RecommendationsByEvaluator calculateRecommendationByEvaluator(EvaluatorProfileMatrix evaluatorProfileMatrix, ItemTagMatrix itemTagMatrix, Evaluator evaluator) {
        SimilarityEvaluatorProfile similarityEvaluatorProfile = new SimilarityEvaluatorProfile(evaluatorProfileMatrix);
        SimilarityEvaluatorContent similarityEvaluatorContent = new SimilarityEvaluatorContent(
                similarityEvaluatorProfile.getContent(),
                itemTagMatrix.getContent(),
                primaryMatrix.getItems(),
                primaryMatrix.getTags()
        );

        startRecommendation = LocalDateTime.now();
        return getRecommendations(evaluator, similarityEvaluatorContent);
    }

    private RecommendationsByEvaluator getRecommendations(Evaluator evaluator, SimilarityEvaluatorContent similarityEvaluatorContent) {
        RecommendationsByEvaluator recommendationsByEvaluator = new RecommendationsByEvaluator();
        recommendationsByEvaluator.setEvaluator(evaluator);
        List<Recommendation> recommendations = new ArrayList<>();
        Integer evaluatorRow = primaryMatrix.getEvaluators().indexOf(evaluator);

        for(int j = 0; j < primaryMatrix.getColSize(); j++) {
            if (primaryMatrix.getContent()[evaluatorRow][j] == null) {
                Double recommendationScore = RecommendationUtils.roundValue(similarityEvaluatorContent.getRecommendationForItemIdx(j), decimalPrecision);

                if (recommendationScore >= passingScore) {
                    recommendationsPositions.add(registerRecommendationPosition(evaluatorRow, j));
                    recommendations.add(buildRecommendation(recommendationScore, evaluator, j, similarityEvaluatorContent));
                }
            }
        }

        recommendationsByEvaluator.setRecommendations(recommendations);
        recommendationsByEvaluator.setMatrixId(recommendationUtils.getNewMatrixIndex(primaryMatrix.getProject()));

        return recommendationsByEvaluator;
    }

    private CellPosition registerRecommendationPosition(Integer row, Integer column) {
        CellPosition recommendationPosition = new CellPosition();

        recommendationPosition.setRow(row);
        recommendationPosition.setColumn(column);

        return recommendationPosition;
    }

    private Recommendation buildRecommendation(Double score, Evaluator evaluator, Integer itemColumnIdx,
        SimilarityEvaluatorContent similarityEvaluatorContent) {
        Item item = similarityEvaluatorContent.getItemByIdx(itemColumnIdx);
        return recommendationUtils.buildRecommendation(score, startRecommendation, 2, item, evaluator,
                primaryMatrix.getProject());
    }

    @Override
    public List<CellPosition> getRecommendationsPositions() {
        return recommendationsPositions;
    }
}
