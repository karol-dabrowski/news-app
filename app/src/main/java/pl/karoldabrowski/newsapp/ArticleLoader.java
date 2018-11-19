package pl.karoldabrowski.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = ArticleLoader.class.getSimpleName();
    private String url;

    public ArticleLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public Object loadInBackground() {
        if (url == null) {
            return null;
        }

        List<Article> articles = Utils.fetchData(url);
        return articles;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
