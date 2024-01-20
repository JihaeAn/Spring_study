package study.spring.karlo.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class KarloResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("model_version")
    private String modelVersion;

    @JsonProperty("images")
    private List<Image> images;

    @Data
    public static class Image {
        @JsonProperty("id")
        private String id;

        @JsonProperty("seed")
        private Integer seed;

        @JsonProperty("image")
        private String image;

        @JsonProperty("nsfw_content_detected")
        private Boolean nsfwContentDetected;

        @JsonProperty("nsfw_score")
        private Double nsfwScore;
    }

}
