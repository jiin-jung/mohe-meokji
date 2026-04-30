package com.mohemeokji.mohemeokji.domain.ingredient.service;

import com.mohemeokji.mohemeokji.domain.ingredient.dto.ExpiryPolicyDto;
import com.mohemeokji.mohemeokji.domain.ingredient.dto.IngredientShelfLifeResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class IngredientShelfLifeService {

    private static final ShelfLifePolicy DEFAULT_POLICY =
            new ShelfLifePolicy(7, "REFRIGERATED", true, 2, "FRESH");

    private final Map<String, ShelfLifePolicy> policies = new LinkedHashMap<>();

    public IngredientShelfLifeService() {
        registerFresh("삼겹살", 3, "REFRIGERATED", 1);
        registerFresh("목살", 3, "REFRIGERATED", 1);
        registerFresh("소고기", 3, "REFRIGERATED", 1);
        registerFresh("닭가슴살", 2, "REFRIGERATED", 1);
        registerFresh("닭", 2, "REFRIGERATED", 1);
        registerFresh("달걀", 14, "REFRIGERATED", 3);
        registerFresh("계란", 14, "REFRIGERATED", 3);
        registerFresh("우유", 7, "REFRIGERATED", 2);
        registerFresh("두부", 5, "REFRIGERATED", 2);
        registerFresh("양파", 30, "ROOM", 5);
        registerFresh("대파", 7, "REFRIGERATED", 2);
        registerFresh("감자", 30, "ROOM", 5);
        registerFresh("고구마", 21, "ROOM", 4);
        registerFresh("당근", 14, "REFRIGERATED", 3);
        registerFresh("오이", 5, "REFRIGERATED", 2);
        registerFresh("상추", 5, "REFRIGERATED", 2);
        registerFresh("배추", 14, "REFRIGERATED", 3);
        registerLongTerm("김치", 30, "REFRIGERATED", 7);
        registerLongTerm("고추장", 180, "REFRIGERATED", 30);
        registerLongTerm("된장", 180, "REFRIGERATED", 30);
        registerLongTerm("간장", 365, "ROOM", 30);
        registerLongTerm("식용유", 180, "ROOM", 30);
        registerLongTerm("참기름", 90, "ROOM", 21);
        registerLongTerm("버터", 30, "REFRIGERATED", 7);
        registerOptional("소금", 365, "ROOM");
        registerOptional("설탕", 365, "ROOM");
    }

    public IngredientShelfLifeResponse getShelfLife(String ingredientName) {
        ShelfLifeMatch match = findPolicy(ingredientName);
        return new IngredientShelfLifeResponse(ingredientName, match.matchedName(), match.policy().shelfLifeDays(), toExpiryPolicy(match.policy()));
    }

    public int resolveShelfLifeDays(String ingredientName) {
        return findPolicy(ingredientName).policy().shelfLifeDays();
    }

    public ExpiryPolicyDto getExpiryPolicy(String ingredientName) {
        return toExpiryPolicy(findPolicy(ingredientName).policy());
    }

    private ShelfLifeMatch findPolicy(String ingredientName) {
        String normalizedInput = normalize(ingredientName);
        if (!StringUtils.hasText(normalizedInput)) return new ShelfLifeMatch("기본값", DEFAULT_POLICY);
        for (Map.Entry<String, ShelfLifePolicy> entry : policies.entrySet()) {
            String normalizedKey = normalize(entry.getKey());
            if (normalizedInput.contains(normalizedKey) || normalizedKey.contains(normalizedInput)) {
                return new ShelfLifeMatch(entry.getKey(), entry.getValue());
            }
        }
        return new ShelfLifeMatch("기본값", DEFAULT_POLICY);
    }

    private ExpiryPolicyDto toExpiryPolicy(ShelfLifePolicy policy) {
        return new ExpiryPolicyDto(policy.trackExpiry(), policy.alertThresholdDays(), policy.displayMode(), policy.storageType());
    }

    private void registerFresh(String name, int days, String storage, int alert) {
        policies.put(name, new ShelfLifePolicy(days, storage, true, alert, "FRESH"));
    }

    private void registerLongTerm(String name, int days, String storage, int alert) {
        policies.put(name, new ShelfLifePolicy(days, storage, true, alert, "LONG_TERM"));
    }

    private void registerOptional(String name, int days, String storage) {
        policies.put(name, new ShelfLifePolicy(days, storage, false, null, "OPTIONAL"));
    }

    private String normalize(String value) {
        if (!StringUtils.hasText(value)) return "";
        return Normalizer.normalize(value, Normalizer.Form.NFKC).replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    }

    private record ShelfLifePolicy(int shelfLifeDays, String storageType, boolean trackExpiry, Integer alertThresholdDays, String displayMode) {}
    private record ShelfLifeMatch(String matchedName, ShelfLifePolicy policy) {}
}