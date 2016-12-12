package fxtrader.com.app.entity;

/**
 * Created by zhangyuzhu on 2016/12/6.
 */
public class LoginResponseEntity extends CommonResponse {

        private String token_type;
        private String access_token;
        private long expires_in;

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public long getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(long expires_in) {
            this.expires_in = expires_in;
        }

        @Override
        public String toString() {
            return "ObjectBean{" +
                    "token_type='" + token_type + '\'' +
                    ", access_token='" + access_token + '\'' +
                    ", expires_in=" + expires_in +
                    '}';
        }

}
