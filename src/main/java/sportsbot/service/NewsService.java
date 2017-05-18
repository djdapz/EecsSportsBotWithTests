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
/**
 * Created by devondapuzzo on 5/15/17.
 */
@Service
public class NewsService
{
    public Story getNewsStory(QuestionContext qc){
        assert(qc.getQuestionType() == QuestionType.NEWS);
//        assert(qc.getTeam() != null);
//        assert(qc.getSport() != null);
        assert(qc.getTemporalContext() != null);
        assert(qc.getQuestion() != null);
        assert(!qc.isClarification());

        StringBuilder sb = new StringBuilder();
        if (qc.getPlayer() != null) {
            sb.append(qc.getPlayer().getFirstName());
            sb.append("+");
            sb.append(qc.getPlayer().getLastName());
            sb.append("+");
            sb.append(qc.getTeam().getName());
            sb.append("+");
            sb.append(qc.getTemporalContext().toString());
        } else if (qc.getTeam() != null){
            sb.append(qc.getTeam().getName());
            if (qc.getTeam().getCity() != null) {
                sb.append("+");
                sb.append(qc.getTeam().getCity().getName());
            }
            sb.append("+").append(qc.getTemporalContext().toString());
        } else if (qc.getPreviousQuestion().getPlayer() != null) {
            sb.append(qc.getPreviousQuestion().getPlayer().getFirstName());
            sb.append("+");
            sb.append(qc.getPreviousQuestion().getPlayer().getLastName());
            sb.append("+");
            sb.append(qc.getPreviousQuestion().getTeam().getName());
            sb.append("+");
            sb.append(qc.getPreviousQuestion().getTemporalContext().toString());
        } else {
            sb.append(qc.getPreviousQuestion().getTeam().getName());
            if (qc.getPreviousQuestion().getTeam().getCity() != null) {
                sb.append("+");
                sb.append(qc.getPreviousQuestion().getTeam().getCity().getName());
            }
            sb.append("+").append(qc.getPreviousQuestion().getTemporalContext().toString());
        }

        HttpClient httpclient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder("https://api.cognitive.microsoft.com/bing/v5.0/search");

            builder.setParameter("q", sb.toString());
//            builder.setParameter("count", "1");
            builder.setParameter("mkt", "en-us");
//            builder.setParameter("safesearch", "Moderate");

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", "9f4ef49129f345138d77a8bc5c599923");

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                JSONObject temp = new JSONObject(EntityUtils.toString(entity));
//                System.out.println(temp.toString(4));
//                System.out.println(temp.keys().toString());

                JSONObject temp2 = (JSONObject)temp.get(temp.keys().next());
//                System.out.println(temp2.toString(4));
                JSONArray news = temp2.getJSONArray("value");
//                System.out.println(((JSONObject)news.get(0)).keySet());
//                System.out.println(((JSONObject)news.get(0)).getString("name"));
//                System.out.println(((JSONObject)news.get(0)).getString("description"));
//                System.out.println(((JSONObject)news.get(0)).getString("url"));


                Integer newsKey = (int)Math.floor(Math.random() * news.length());

                String url = ((JSONObject)news.get(newsKey)).getString("url");


                Configuration configuration = new Configuration();
                configuration.setMinBytesForImages(4500);
                configuration.setLocalStoragePath("/tmp/goose");
                configuration.setEnableImageFetching(false); // i don't care about the image, just want text, this is much faster!
                configuration.setImagemagickConvertPath("/opt/local/bin/convert");
                Goose goose = new Goose(configuration);
                Article article = goose.extractContent(url);
//                System.out.println(article.cleanedArticleText());
                return new Story(article.cleanedArticleText(), url);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
