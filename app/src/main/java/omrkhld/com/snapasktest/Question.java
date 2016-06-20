package omrkhld.com.snapasktest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Omar on 19/6/2016.
 */
public class Question implements Serializable {

    public Question() {
    }

    private String status;
    public static class Data implements Serializable {

        public Data() {
        }

        private int ID;

        public static class AskedBy implements Serializable {

            public AskedBy() {
            }

            private int ID;
            private String email;
            private String username;

            private static class Role implements Serializable {
                public Role() {
                }

                private int ID;
                private String name;

                public int getID() { return ID; }
                public String getName() { return name; }

                public void setID(int id) { ID = id; }
                public void setName(String n) { name = n; }
            }

            private Role role;
            private String imgURL;

            public int getID() { return ID; }
            public String getEmail() { return email; }
            public String getUsername() { return username; }
            @JsonProperty("role")
            public Role getRole() { return role; }
            @JsonProperty("profile_pic_url")
            public String getImgURL() { return imgURL; }

            public void setID(int id) { ID = id; }
            public void setEmail(String e) { email = e; }
            public void setRole(Role r) { role = r; }
            public void setUsername(String u) { username = u; }
            public void setImgURL(String i) { imgURL = i; }
        }
        private AskedBy askedBy;
        private String description;
        private String status;
        private String user_ID;
        private String answer_tutor_ID;

        public static class AnsweredBy implements Serializable {

            public AnsweredBy() {
            }

            private int ID;
            private String email;
            private String username;

            private static class Role implements Serializable {

                public Role() {
                }

                private int ID;
                private String name;

                public int getID() { return ID; }
                public String getName() { return name; }

                public void setID(int id) { ID = id; }
                public void setName(String n) { name = n; }
            }

            private Role role;
            private String imgURL;

            public int getID() { return ID; }
            public String getEmail() { return email; }
            public String getUsername() { return username; }
            @JsonProperty("role")
            public Role getRole() { return role; }
            @JsonProperty("profile_pic_url")
            public String getImgURL() { return imgURL; }

            public void setID(int id) { ID = id; }
            public void setEmail(String e) { email = e; }
            public void setRole(Role r) { role = r; }
            public void setUsername(String u) { username = u; }
            public void setImgURL(String i) { imgURL = i; }
        }

        private AnsweredBy answeredBy;
        private String createdAt;

        public static class Subject implements Serializable {

            public Subject() {
            }

            private int ID;
            private String abbr;
            private String description;

            public static class Region implements Serializable {

                public Region() {
                }

                private int ID;
                private String name;
                private String full_name;

                public int getID() { return ID; }
                public String getName() { return name; }
                @JsonProperty("full_name")
                public String getFullname() { return full_name; }

                public void setID(int id) { ID = id; }
                public void setName(String n) { name = n; }
                public void setFullname(String fn) { full_name = fn; }
            }

            private Region region;

            public int getID() { return ID; }
            public String getAbbr() { return abbr; }
            public String getDescription() { return description; }
            @JsonProperty("region")
            public Region getRegion() { return region; }

            public void setID(int id) { ID = id; }
            public void setAbbr(String a) { abbr = a; }
            public void setDescription(String d) { description = d; }
            public void setRegion(Region r) { region = r; }
        }

        private Subject subject;
        private String imgURL;
        private int userRating;

        public int getID() { return ID; }
        @JsonProperty("asked_by")
        public AskedBy getAskedBy() { return askedBy; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
        @JsonProperty("user_id")
        public String getUserID() { return user_ID; }
        @JsonProperty("answer_tutor_id")
        public String getTutorID() { return answer_tutor_ID; }
        @JsonProperty("answered_by")
        public AnsweredBy getAnsweredBy() { return answeredBy; }
        @JsonProperty("created_at")
        public String getCreatedAt() { return createdAt; }
        @JsonProperty("subject")
        public Subject getSubject() { return subject; }
        @JsonProperty("picture_url")
        public String getImgURL() { return imgURL; }
        @JsonProperty("user_rating")
        public int getUserRating() { return userRating; }

        public void setID(int id) { ID = id; }
        public void setAskedBy(AskedBy a) { askedBy = a; }
        public void setDescription(String d) { description = d; }
        public void setStatus(String s) { status = s; }
        public void setUserID(String uid) { user_ID = uid; }
        public void setTutorID(String tid) { answer_tutor_ID = tid; }
        public void setAnsweredBy(AnsweredBy a) { answeredBy = a; }
        public void setCreatedAt(String c) { createdAt = c; }
        public void setSubject(Subject s) { subject = s; }
        public void setImgURL(String i) { imgURL = i; }
        public void setUserRating(int r) { userRating = r; }
    }

    private Data data;

    public String getStatus() { return status; }
    public Data getData() { return data; }

    public void setStatus(String s) { status = s; }
    public void setData(Data d) { data = d; }
}
