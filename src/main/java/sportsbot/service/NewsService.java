package sportsbot.service;

import org.springframework.stereotype.Service;
import sportsbot.enums.QuestionType;
import sportsbot.model.QuestionContext;
import sportsbot.model.Story;

import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//import org.apache.http.entity.StringEntity;
// json package
import org.json.JSONArray;
import org.json.JSONObject;
// goose package
import com.gravity.goose.*;
// rake

// list
import java.util.*;

/**
 * Created by devondapuzzo on 5/15/17.
 */
/*
TODO:
add white list site:(espn.com)
extract main content by goose
extract key words

 */
@Service
public class NewsService
{
    private static List<String> whiteList = new ArrayList<String>();
    public NewsService() {
        whiteList.add("espn");
        whiteList.add("yahoo");
        whiteList.add("cbssports");
//        whiteList.add("nbcsports");
//        whiteList.add("foxsports");
//        whiteList.add("cnn");
//        whiteList.add("nytimes");
//        whiteList.add("bleacherreport");
//        whiteList.add("sbnation");
//        whiteList.add("rantsports");
    }
    private static String getWhiteListString() {
        StringBuilder sb = new StringBuilder();
        sb.append("site:(");
        for (Object str : whiteList.toArray()) {
//            sb.append("");
            sb.append(str);
            sb.append(".com ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(")");
        return sb.toString();
    }
    private static boolean checkWhiteList(String url) {
        for (String whiteName : whiteList) {
//            System.out.println(whiteName);
            if (url.contains(whiteName)) return true;
        }
        return false;
    }
    public Story getNewsStory(QuestionContext qc){
        assert(qc.getQuestionType() == QuestionType.NEWS);
//        assert(qc.getTeam() != null);
//        assert(qc.getSport() != null);
        assert(qc.getTemporalContext() != null);
        assert(qc.getQuestion() != null);
        assert(!qc.isClarification());

        StringBuilder sb = new StringBuilder();
//        sb.append(getWhiteListString());
//        sb.append(" ");
        if (qc.getPlayer() != null) {
            sb.append(qc.getPlayer().getFirstName());
            sb.append(" ");
            sb.append(qc.getPlayer().getLastName());
            sb.append(" ");
            sb.append(qc.getTeam().getName());
//            sb.append("+");
//            sb.append(qc.getTemporalContext().toString());
        } else if (qc.getTeam() != null){
            sb.append(qc.getTeam().getName());
            if (qc.getTeam().getCity() != null) {
                sb.append(" ");
                sb.append(qc.getTeam().getCity().getName());
            }
//            sb.append(" ").append(qc.getTemporalContext().toString());
        } else if (qc.getPreviousQuestion().getPlayer() != null) {
            sb.append(qc.getPreviousQuestion().getPlayer().getFirstName());
            sb.append(" ");
            sb.append(qc.getPreviousQuestion().getPlayer().getLastName());
            sb.append(" ");
            sb.append(qc.getPreviousQuestion().getTeam().getName());
//            sb.append("+");
//            sb.append(qc.getPreviousQuestion().getTemporalContext().toString());
        } else {
            sb.append(qc.getPreviousQuestion().getTeam().getName());
            if (qc.getPreviousQuestion().getTeam().getCity() != null) {
                sb.append(" ");
                sb.append(qc.getPreviousQuestion().getTeam().getCity().getName());
            }
//            sb.append("+").append(qc.getPreviousQuestion().getTemporalContext().toString());
        }
//        https://www.bing.com/search?q=site%3A+%28espn.com+yahoo.com+cbssports.com+nbcsports.com+foxsports.com+cnn.com+nytimes.com+bleacherreport.com+sbnation.com+rantsports.com+%29+Cubs%2BChicago%2BTODAY&go=Submit&qs=n&form=QBLH&sp=-1&pq=site%3A+%28espn.com+yahoo.com+cbssports.com+nbcsports.com+foxsports.com+cnn.com+nytimes.com+bleacherreport.com+sbnation.com+rantsports.com+%29+cubs%2Bchicago%2Btoday&sc=0-155&sk=&cvid=164E1EF275874899BEBD56A50F818D5C
//        System.out.println(sb.toString());
        HttpClient httpclient = HttpClients.createDefault();

        try {
//            URIBuilder builder = new URIBuilder("https://api.cognitive.microsoft.com/bing/v5.0/search");
            URIBuilder builder = new URIBuilder(("https://api.cognitive.microsoft.com/bing/v7.0/news/search"));
            builder.setParameter("q", sb.toString());
            builder.setParameter("count", "20");
            builder.setParameter("mkt", "en-us");
            builder.setParameter("safesearch", "Moderate");

            URI uri = builder.build();
            System.out.println(uri.toString());
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", "e9f923377a944bc8885330a050af76f1");

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                JSONObject temp = new JSONObject(EntityUtils.toString(entity));
//                System.out.println(temp.toString(4));
//                System.out.println(temp.keySet().toString());
//                JSONObject temp2 = null;
//                if (temp.has("news")) {
//                    temp2 = (JSONObject)temp.get("news");
//                } else if (temp.has("webPages")) {
//                    temp2 = (JSONObject)temp.get("webPages");
//                } else {
//                    return null;
//                }
//                JSONArray news = temp2.getJSONArray("value");
//                System.out.print(news.toString(4));
                JSONArray news = temp.getJSONArray("value");
                String url = null;
                Integer newsKey = (int)Math.floor(Math.random() * news.length());
                url = ((JSONObject)news.get(newsKey)).getString("url");
//                do {
//                    Integer newsKey = (int)Math.floor(Math.random() * news.length());
//                    url = ((JSONObject)news.get(newsKey)).getString("url");
//                    news.remove(newsKey);
////                    System.out.println(url);
//                } while(!checkWhiteList(url) && news.length() > 0);
//
//                if (news.length() == 0) return null;

                Configuration configuration = new Configuration();
                configuration.setMinBytesForImages(4500);
                configuration.setLocalStoragePath("/tmp/goose");
                configuration.setEnableImageFetching(false); // i don't care about the image, just want text, this is much faster!
                configuration.setImagemagickConvertPath("/opt/local/bin/convert");
                Goose goose = new Goose(configuration);
                Article article = goose.extractContent(url);
//                System.out.println(article.cleanedArticleText());
//                if (article.cleanedArticleText().length() < 20) {
//                    return getNewsStory(qc);
//                } else {
//                    return new Story(article.cleanedArticleText(), ((JSONObject)news.get(newsKey)).getString("description"), url);
//                }
                return new Story(article.cleanedArticleText(), ((JSONObject)news.get(newsKey)).getString("description"), url);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            getNewsStory(qc);
        }
        return null;
    }
}
