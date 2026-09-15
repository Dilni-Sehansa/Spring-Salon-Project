package com.example.Spring_Salon_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiStyleDnaResponseDTO {
        private String faceShape;
        private String skinUndertone;
        private String hairType;
        private String overallLookSummary;
        private List<String> recommendedHairStyles;
        private List<String> recommendedHairColors;
        private List<RecommendedService> recommendedServices;
        private List<RecommendedProduct> recommendedProducts;
        private String packageSuggestion;
        private Double estimatedPackagePrice;
        private String confidenceNote;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RecommendedService {
            private Long serviceId;
            private String serviceName;
            private String reason;
            private Double price;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RecommendedProduct {
            private Long productId;
            private String productName;
            private String reason;
            private Double price;
        }
}
