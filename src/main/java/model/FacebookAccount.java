    package model;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class FacebookAccount {
        private String id;
        private String email;
        private String name;
        private String picture;
    }
