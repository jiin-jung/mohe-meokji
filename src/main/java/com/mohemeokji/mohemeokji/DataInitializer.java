package com.mohemeokji.mohemeokji;

import com.mohemeokji.mohemeokji.domain.AuthProvider;
import com.mohemeokji.mohemeokji.domain.HouseholdType;
import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.domain.UserRole;
import com.mohemeokji.mohemeokji.repository.IngredientRepository;
import com.mohemeokji.mohemeokji.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    public void run(String... args) throws Exception {
        // 유저 1, 2 생성 (프론트가 2번 고정이므로 2번까지 생성)
        if (!userRepository.existsById(1L)) {
            userRepository.save(User.builder()
                    .email("user1@test.com")
                    .nickname("유저1")
                    .provider(AuthProvider.LOCAL)
                    .role(UserRole.USER)
                    .householdType(HouseholdType.SINGLE)
                    .build());
        }
        if (!userRepository.existsById(2L)) {
            User user2 = userRepository.save(User.builder()
                    .email("test@example.com")
                    .nickname("테스트유저")
                    .provider(AuthProvider.LOCAL)
                    .role(UserRole.USER)
                    .householdType(HouseholdType.SINGLE)
                    .build());

            LocalDate today = LocalDate.now();
            // 초기 재료 데이터 추가
            ingredientRepository.save(Ingredient.builder().name("삼겹살").quantity(300.0).unit("g").category("육류").purchaseDate(today).expiryDate(today.plusDays(3)).user(user2).build());
            ingredientRepository.save(Ingredient.builder().name("목살").quantity(200.0).unit("g").category("육류").purchaseDate(today).expiryDate(today.plusDays(3)).user(user2).build());
            ingredientRepository.save(Ingredient.builder().name("양파").quantity(2.0).unit("개").category("채소").purchaseDate(today).expiryDate(today.plusDays(30)).user(user2).build());
            ingredientRepository.save(Ingredient.builder().name("대파").quantity(1.0).unit("단").category("채소").purchaseDate(today).expiryDate(today.plusDays(7)).user(user2).build());
            ingredientRepository.save(Ingredient.builder().name("고추장").quantity(500.0).unit("g").category("조미료").purchaseDate(today).expiryDate(today.plusDays(180)).user(user2).build());
            ingredientRepository.save(Ingredient.builder().name("달걀").quantity(10.0).unit("개").category("기타").purchaseDate(today).expiryDate(today.plusDays(14)).user(user2).build());
        }
    }
}
