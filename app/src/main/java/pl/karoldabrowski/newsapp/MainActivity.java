package pl.karoldabrowski.newsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>>,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String API_KEY = "";
    private static final String API_URL = "https://content.guardianapis.com/search";

    private TextView emptyStateTextView;
    private View progressBar;
    private ArticleAdapter adapter;
    private String section = "football";

    private static final int ARTICLE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.createNavDrawer();

        ListView articlesListView = (ListView) findViewById(R.id.list);
        emptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        progressBar = findViewById(R.id.preloader);
        adapter = new ArticleAdapter(this, new ArrayList<Article>());
        articlesListView.setEmptyView(emptyStateTextView);
        articlesListView.setAdapter(adapter);

        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article clickedArticle = adapter.getItem(position);
                Uri uri = Uri.parse(clickedArticle.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if(isConnected){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            emptyStateTextView.setText(getText(R.string.connection_problem_text));
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String maxArticles = sharedPrefs.getString(
                getString(R.string.settings_max_number_of_articles_key),
                getString(R.string.settings_max_number_of_articles_default));

        Uri baseUri = Uri.parse(API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        uriBuilder.appendQueryParameter("page-size", maxArticles);

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        progressBar.setVisibility(View.GONE);

        if(articles != null && !articles.isEmpty()) {
            adapter.addAll(articles);
        } else {
            emptyStateTextView.setText(getText(R.string.nothing_found_text));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String oldSectionValue = section;
        emptyStateTextView.setText("");

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (id == R.id.nav_football) {
            section = "football";
        } else if (id == R.id.nav_games) {
            section = "games";
        } else if (id == R.id.nav_fashion) {
            section = "fashion";
        } else {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        if(!isConnected) {
            emptyStateTextView.setText(getText(R.string.connection_problem_text));
        } else if(section != oldSectionValue) {
            progressBar.setVisibility(View.VISIBLE);
            adapter.clear();
            getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null, this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createNavDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}
