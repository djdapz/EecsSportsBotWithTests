package sportsbot.model;

/**
 * Created by devondapuzzo on 5/28/17.
 */
public class Param {


        private String title;
        private String value;

        public Param(String title, String value) {
            this.title = title;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


}
