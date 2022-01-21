package com.srh.api.config;

import com.srh.api.builder.*;
import com.srh.api.model.*;
import com.srh.api.repository.*;
import com.srh.api.utils.BcriptyUtil;
import com.srh.api.utils.PasswordUtil;

import org.h2.engine.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DbSeeder {
    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRatingRepository itemRatingRepository;

    @Autowired
    private EvaluatorRepository evaluatorRepository;


    @Autowired
    private ProjectRepository projectRepository;

    private Profile adminProfile;
    private Profile userProfile;

    public boolean seed() {
        createProfileAdmin();
        createProfileUser();

        createApiUserAdmin();
        createApiUserClient();

        createAlgorithms();
        createTestProject();

        return true;
    }

    private void createTestProject() {
        Admin admin = AdminBuilder.anAdmin()
            .withId(1)
            .withName("Mr. Admin")
            .withLogin("adm")
            .withEmail("admin@admin.com")
            .withPassword(BcriptyUtil.encripty("123456"))
            .build();
        adminRepository.save(admin);
        Project testProject = ProjectBuilder.aProject()
            .withId(1)
            .withDate(LocalDate.now())
            .withDescription("Test Project for Debugging")
            .withName("test")
            .withAdmin(admin)
            .withLastMatrixId(0)
            .build();

        Item celular = ItemBuilder.anItem()
            .withId(1)
            .withName("Celular")
            .withDescription("Um celular")
            .withProject(testProject)
            .build();
        Item guitarra = ItemBuilder.anItem()
            .withId(2)
            .withName("Guitarra")
            .withDescription("Uma guitarra")
            .withProject(testProject)
            .build();
        Item cadeira = ItemBuilder.anItem()
            .withId(3)
            .withName("Cadeira")
            .withDescription("Uma cadeira")
            .withProject(testProject)
            .build();
        Item bateria = ItemBuilder.anItem()
            .withId(4)
            .withName("Bateria")
            .withDescription("Uma bateria")
            .withProject(testProject)
            .build();

        Tag musical = TagBuilder.aTag()
            .withId(1)
            .withName("Musical")
            .withItens(Arrays.asList(guitarra,bateria))
            .build();
        Tag tecnologia = TagBuilder.aTag()
            .withId(2)
            .withName("Tecnologia")
            .withItens(Arrays.asList(celular))
            .build();
        Tag lazer = TagBuilder.aTag()
            .withId(3)
            .withName("Lazer")
            .withItens(Arrays.asList(celular,guitarra,bateria))
            .build();
        Tag precoAlto = TagBuilder.aTag()
            .withId(4)
            .withName("Preço Alto")
            .withItens(Arrays.asList(celular,guitarra,cadeira,bateria))
            .build();
        Tag grande = TagBuilder.aTag()
            .withId(5)
            .withName("Grande")
            .withItens(Arrays.asList(guitarra,cadeira,bateria))
            .build();
        
        Evaluator alberto = EvaluatorBuilder.anEvaluator()
            .withId(1)
            .withName("Alberto").withEmail("Alberto@alberto.com")
            .withLogin("alberto").withPassword(BcriptyUtil.encripty("123456"))
            .build();
        Evaluator bianca = EvaluatorBuilder.anEvaluator()
            .withId(2)
            .withName("Bianca").withEmail("Bianca@bianca.com")
            .withLogin("bianca").withPassword(BcriptyUtil.encripty("123456"))
            .build();
        Evaluator carlos = EvaluatorBuilder.anEvaluator()
            .withId(3)
            .withName("Carlos").withEmail("Carlos@carlos.com")
            .withLogin("carlos").withPassword(BcriptyUtil.encripty("123456"))
            
            .build();
        Evaluator daniele = EvaluatorBuilder.anEvaluator()
            .withId(4)
            .withName("Daniele").withEmail("Daniele@daniele.com")
            .withLogin("daniele").withPassword(BcriptyUtil.encripty("123456"))
            .build();
        
        ItemRating albCel = ItemRatingBuilder.anItemRating()
            .withDate(LocalDateTime.now())
            .withScore(4.5)
            .withId(ItemRatingPKBuilder.anItemRatingPK()
                .withEvaluator(alberto).withItem(celular)
                .build())
            .build();
        ItemRating albGui = ItemRatingBuilder.anItemRating()
            .withDate(LocalDateTime.now())
            .withScore(4.0)
            .withId(ItemRatingPKBuilder.anItemRatingPK()
                .withEvaluator(alberto).withItem(guitarra)
                .build())
            .build();
        ItemRating albCad = ItemRatingBuilder.anItemRating()
            .withDate(LocalDateTime.now())
            .withScore(5.0)
            .withId(ItemRatingPKBuilder.anItemRatingPK()
                .withEvaluator(alberto).withItem(cadeira)
                .build())
            .build();
        ItemRating biaGui = ItemRatingBuilder.anItemRating()
            .withDate(LocalDateTime.now())
            .withScore(3.5)
            .withId(ItemRatingPKBuilder.anItemRatingPK()
                .withEvaluator(bianca).withItem(guitarra)
                .build())
            .build();
        ItemRating biaCad = ItemRatingBuilder.anItemRating()
            .withDate(LocalDateTime.now())
            .withScore(1.5)
            .withId(ItemRatingPKBuilder.anItemRatingPK()
                .withEvaluator(bianca).withItem(cadeira)
                .build())
            .build();
        ItemRating biaBat = ItemRatingBuilder.anItemRating()
            .withDate(LocalDateTime.now())
            .withScore(4.0)
            .withId(ItemRatingPKBuilder.anItemRatingPK()
                .withEvaluator(bianca).withItem(bateria)
                .build())
            .build();
        ItemRating carCel = ItemRatingBuilder.anItemRating()
            .withDate(LocalDateTime.now())
            .withScore(5.0)
            .withId(ItemRatingPKBuilder.anItemRatingPK()
                .withEvaluator(carlos).withItem(celular)
                .build())
            .build();
        ItemRating carGui = ItemRatingBuilder.anItemRating()
            .withDate(LocalDateTime.now())
            .withScore(3.0)
            .withId(ItemRatingPKBuilder.anItemRatingPK()
                .withEvaluator(carlos).withItem(guitarra)
                .build())
            .build();
        ItemRating danCad = ItemRatingBuilder.anItemRating()
            .withDate(LocalDateTime.now())
            .withScore(4.0)
            .withId(ItemRatingPKBuilder.anItemRatingPK()
                .withEvaluator(daniele).withItem(cadeira)
                .build())
            .build();
        ItemRating danBat = ItemRatingBuilder.anItemRating()
            .withDate(LocalDateTime.now())
            .withScore(2.5)
            .withId(ItemRatingPKBuilder.anItemRatingPK()
                .withEvaluator(daniele).withItem(bateria)
                .build())
            .build();

        projectRepository.save(testProject);

        List<Evaluator> listEvaluators = Arrays.asList(alberto,bianca,carlos,daniele);
        for(Evaluator ev : listEvaluators){
            ev.setProjects(Arrays.asList(testProject));
        }
        evaluatorRepository.saveAll(listEvaluators);

        List<Item> listItems = Arrays.asList(celular, guitarra, cadeira, bateria);
        itemRepository.saveAll(listItems);

        List<Tag> listTags = Arrays.asList(musical, tecnologia, lazer, precoAlto, grande);
        tagRepository.saveAll(listTags);

        List<ItemRating> listItemRatings = Arrays.asList(
            albCel,albGui,albCad,
                   biaGui,biaCad,biaBat,
            carCel,carGui,
                          danCad,danBat);
        itemRatingRepository.saveAll(listItemRatings);


    }

    private void createApiUserAdmin() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(adminProfile);
        profiles.add(userProfile);

        ApiUser apiUser = ApiUserBuilder.anApiUser()
                .withId(1)
                .withEmail("admin@email.com")
                .withName("admin")
                .withLogin("admin")
                .withPassword(BcriptyUtil.encripty("123456"))
                .withProfiles(profiles)
                .withIsAdmin(true)
                .build();

        apiUserRepository.save(apiUser);
    }

    private void createApiUserClient() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(userProfile);

        ApiUser apiUser = ApiUserBuilder.anApiUser()
                .withId(2)
                .withEmail("client@email.com")
                .withName("client")
                .withLogin("client")
                .withPassword(BcriptyUtil.encripty("123456"))
                .withProfiles(profiles)
                .withIsAdmin(true)
                .build();

        apiUserRepository.save(apiUser);
    }

    private void createProfileAdmin() {
        Profile profile = ProfileBuilder.aProfile()
                .withId(1)
                .withName("ROLE_ADMIN")
                .build();

        adminProfile = profile;
        profileRepository.save(profile);
    }

    private void createProfileUser() {
        Profile profile = ProfileBuilder.aProfile()
                .withId(2)
                .withName("ROLE_USER")
                .build();

        userProfile = profile;
        profileRepository.save(profile);
    }

    private void createAlgorithms() {
        Algorithm algorithm1 = AlgorithmBuilder.anAlgorithm()
                .withId(1)
                .withName("Filtragem Colaborativa")
                .withTypeRecommendation(TypeRecommendation.COLLABORATIVE)
                .build();

        Algorithm algorithm2 = AlgorithmBuilder.anAlgorithm()
                .withId(2)
                .withName("Filtragem Baseada em Conteúdo")
                .withTypeRecommendation(TypeRecommendation.CONTENT)
                .build();

        Algorithm algorithm3 = AlgorithmBuilder.anAlgorithm()
                .withId(3)
                .withName("Filtragem Híbrida Ponderada")
                .withTypeRecommendation(TypeRecommendation.HYBRID)
                .build();

        Algorithm algorithm4 = AlgorithmBuilder.anAlgorithm()
                .withId(4)
                .withName("Filtragem Híbrida Mista")
                .withTypeRecommendation(TypeRecommendation.HYBRID)
                .build();
        Algorithm algorithm5 = AlgorithmBuilder.anAlgorithm()
                .withId(5)
                .withName("Filtragem Híbrida por Combinação Sequencial")
                .withTypeRecommendation(TypeRecommendation.HYBRID)
                .build();


        algorithmRepository.saveAll(Arrays.asList(
                algorithm1, algorithm2,
                algorithm3, algorithm4,
                algorithm5
        ));
    }
}
