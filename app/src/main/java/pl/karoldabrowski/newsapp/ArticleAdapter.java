package pl.karoldabrowski.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(@NonNull Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Article currentArticle = getItem(position);
        TextView articleTitle = listItemView.findViewById(R.id.article_title);
        articleTitle.setText(currentArticle.getTitle());

        if(currentArticle.hasAuthor()) {
            TextView articleAuthor = listItemView.findViewById(R.id.article_author);
            articleAuthor.setText(currentArticle.getAuthor());
        }

        TextView articleSection = listItemView.findViewById(R.id.article_section);
        articleSection.setText(currentArticle.getSection());

        if(currentArticle.hasDate()) {
            TextView articleDate = listItemView.findViewById(R.id.article_date);
            articleDate.setText(currentArticle.getDate().replace('T', ' ').replace("Z", ""));
        }

        return listItemView;
    }
}
