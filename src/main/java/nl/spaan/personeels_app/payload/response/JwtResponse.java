package nl.spaan.personeels_app.payload.response;



public class JwtResponse {

        private String token;

        public JwtResponse(String accessToken) {
            this.token = accessToken;
        }

        public String getAccessToken() {
            return token;
        }

        public void setAccessToken(String accessToken) {
            this.token = accessToken;
        }

    }
