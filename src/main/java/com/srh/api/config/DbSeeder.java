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
        Tag musical = TagBuilder.aTag()
            .withId(1)
            .withName("Musical")
            .build();
        Tag tecnologia = TagBuilder.aTag()
            .withId(2)
            .withName("Tecnologia")
            .build();
        Tag lazer = TagBuilder.aTag()
            .withId(3)
            .withName("Lazer")
            .build();
        Tag precoAlto = TagBuilder.aTag()
            .withId(4)
            .withName("Preço Alto")
            .build();
        Tag grande = TagBuilder.aTag()
            .withId(5)
            .withName("Grande")
            .build();
        List<Tag> listTags = List.of(musical, tecnologia, lazer, precoAlto, grande);
        
        Item celular = ItemBuilder.anItem()
            .withId(1)
            .withName("Celular")
            .withTags(List.of(tecnologia,lazer,precoAlto))
            .build();
        Item guitarra = ItemBuilder.anItem()
            .withId(2)
            .withName("Guitarra")
            .withTags(List.of(musical,lazer,precoAlto,grande))
            .build();
        Item cadeira = ItemBuilder.anItem()
            .withId(3)
            .withName("Cadeira")
            .withTags(List.of(precoAlto,grande))
            .build();
        Item bateria = ItemBuilder.anItem()
            .withId(4)
            .withName("Bateria")
            .withTags(List.of(musical,lazer,precoAlto,grande))
            .build();
        List<Item> listItems = List.of(celular, guitarra, cadeira, bateria);

        Evaluator alberto = EvaluatorBuilder.anEvaluator()
            .withId(1)
            .withName("Alberto").withEmail("Alberto@alberto.com")
            .withPassword(BcriptyUtil.encripty("123456"))
            .build();
        Evaluator bianca = EvaluatorBuilder.anEvaluator()
            .withId(1)
            .withName("Bianca").withEmail("Bianca@bianca.com")
            .withPassword(BcriptyUtil.encripty("123456"))
            .build();
        Evaluator carlos = EvaluatorBuilder.anEvaluator()
            .withId(1)
            .withName("Carlos").withEmail("Carlos@carlos.com")
            .withPassword(BcriptyUtil.encripty("123456"))
            .build();
        Evaluator daniele = EvaluatorBuilder.anEvaluator()
            .withId(1)
            .withName("Daniele").withEmail("Daniele@daniele.com")
            .withPassword(BcriptyUtil.encripty("123456"))
            .build();
        
        List<Evaluator> listEvaluators = List.of(alberto,bianca,carlos,daniele);
        
        Project testProject = ProjectBuilder.aProject()
            .withDate(LocalDate.now())
            .withDescription("Test Project for Debugging")
            .withName("test")
            .build();
        projectRepository.save(testProject);

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

        algorithmRepository.saveAll(Arrays.asList(
                algorithm1, algorithm2,
                algorithm3, algorithm4
        ));
    }
}
