package com.deepra.twitter.data;

public class Media {
    long id;
    String id_str;//": "1128599177041195008",
    int[] indices;//": [82, 105],
    String media_url;//": "http:\/\/pbs.twimg.com\/media\/D6mW-NFWsAA-8Wa.jpg",
    String media_url_https;//": "https:\/\/pbs.twimg.com\/media\/D6mW-NFWsAA-8Wa.jpg",
    String url;//": "https:\/\/t.co\/lKWlsF5zWx",
    String display_url;//": "pic.twitter.com\/lKWlsF5zWx",
    String expanded_url;//": "https:\/\/twitter.com\/mental_floss\/status\/1128599180795097088\/photo\/1",
    String type;//": "photo",
//            "sizes": {
//        "medium": {
//            "w": 1000,
//                    "h": 664,
//                    "resize": "fit"
//        },
//        "thumb": {
//            "w": 150,
//                    "h": 150,
//                    "resize": "crop"
//        },
//        "large": {
//            "w": 1000,
//                    "h": 664,
//                    "resize": "fit"
//        },
//        "small": {
//            "w": 680,
//                    "h": 452,
//                    "resize": "fit"
//        }
//    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_url_https() {
        return media_url_https;
    }

    public void setMedia_url_https(String media_url_https) {
        this.media_url_https = media_url_https;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplay_url() {
        return display_url;
    }

    public void setDisplay_url(String display_url) {
        this.display_url = display_url;
    }

    public String getExpanded_url() {
        return expanded_url;
    }

    public void setExpanded_url(String expanded_url) {
        this.expanded_url = expanded_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
