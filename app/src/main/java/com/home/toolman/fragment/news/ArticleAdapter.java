package com.home.toolman.fragment.news;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.home.toolman.R;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */

public class ArticleAdapter extends GroupRecyclerAdapter<String, Article> {


    private RequestManager mLoader;

    public ArticleAdapter(Context context) {
        super(context);
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<Article>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("今日推荐", create(0));
        map.put("每周热点", create(1));
        map.put("最高评论", create(2));
        titles.add("今日推荐");
        titles.add("每周热点");
        titles.add("最高评论");
        resetGroups(map,titles);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ArticleViewHolder(mInflater.inflate(R.layout.item_list_article, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, Article item, int position) {
        ArticleViewHolder h = (ArticleViewHolder) holder;
        h.mTextTitle.setText(item.getTitle());
        h.mTextContent.setText(item.getContent());
        mLoader.load(item.getImgUrl())
                .into(h.mImageView);
    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTitle,
                mTextContent;
        private ImageView mImageView;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.tv_title);
            mTextContent = itemView.findViewById(R.id.tv_content);
            mImageView = itemView.findViewById(R.id.imageView);
        }
    }


    private static Article create(String title, String content, String imgUrl) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setImgUrl(imgUrl);
        return article;
    }

    private static List<Article> create(int p) {
        List<Article> list = new ArrayList<>();
        if (p == 0) {
            list.add(create("看视频学英语",
                    "危机如何影响历史--【2020国际翻译日：Finding the words for a world in crisis】",
                    "http://www.yarace.com/upload/images/2020/9/3015576968.jpg"));
            list.add(create("开言英语",
                    "Gif到底怎么读？今天终于破案了！Graphics Interchange Format",
                    "http://fanyi-app.baidu.com/static/passage/2020-07/2020-07-06/001/image/datasrc0.jpg"));
            list.add(create("有声励志英文演讲：Diversify your gift",
                    "There lies your greatest secret for success.You've got to discover your gift.And when you discover it,you got to soak it.You got to wring it out,man.You got to diversify it",
                    "https://imagev2.xmcdn.com/storages/56b6-audiofreehighqps/09/94/CMCoOR8DdDLFAAzQHgBinKn4.jpg!strip=1&quality=7&magick=jpg&op_type=5&upload_type=cover&name=web_large&device_type=ios"));
            list.add(create("百度翻译字幕组",
                    "疫情改变了一切，不要忘记关心身处异国他乡的朋友们! 2019-nCoV changes everything",
                    "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2027240073,3294694557&fm=26&gp=0.jpg"));
            list.add(create("英语杂货店",
                    "\"你行你上啊\"不要再说成\"You can you up!\"啦，正确的表达是...put up or shut up",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606927719716&di=d7f5049b40a10b16386b0c868d655a7a&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20151028%2Fmp38301942_1446013674271_6.jpeg"));
        } else if (p == 1) {
            list.add(create(
                    "The Rivals in the Life生活中的对手",
                    "Jaguar is an endangered animal. It is said that there are less than 20 jaguars in the world currently, one of which is now living in the national zoo of Peru. In order to protect this jaguar, Peruvians singled out a pitch of land in the zoo for it, where there are herds of cattle, sheep and deer for the jaguar to eat. ",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606928622624&di=9be5215264657e8dfe9510f0ad4f0093&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_pic%2F16%2F12%2F05%2Fc068cca1cdb238baaf00d4a6c7b0d2f5.jpg"));
            list.add(create(
                    "The Life in Your Twenties 20岁的生活",
                    "On Constant Worrying 关于持续的担忧 Dufu says one of the greatest pieces of advice she ever received was about her constant worrying. “My mentor said, ‘If you would spend less time worrying about choices you don’t have and actually creating those choices, you would",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606928622615&di=e2ac852b15939f088592049ff8cc7f98&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F17%2F12%2F16%2F7153742bd0cbaeee2168a906c81a9a4f.jpg"));
            list.add(create(
                    "汇聚各种经典演讲的网站",
                    "American Rhetoric: The Power of Oratory in the United States",
                    "https://pic4.zhimg.com/80/v2-02346273f65be074e1fb44ec628d507b_720w.jpg?source=1940ef5c"));
        } else if (p == 2) {
            list.add(create(
                    "MOMENTS FROM THE TOP 100 SPEECHES",
                    "John F. Kennedy We dare not forget today that we are the heirs of that first revolution. Let the word go forth from this time and place, to friend and foe alike, that the torch has been passed to a new generation of Americans -- born in this century, tempered by war, disciplined by a hard and bitter peace, proud of our ancient heritage, and unwilling to witness or permit the slow undoing of those human rights to which this nation has always been committed, and to which we are committed today at home and around the world.",
                    "https://pic1.zhimg.com/80/v2-7e590ed569b1b4ef5b1374b462ba7947_720w.jpg?source=1940ef5c"));
            list.add(create(
                    "Emma Watson 2016年关于平权问题的联合国演讲",
                    "联合国妇女署发布“他为她（HeForShe）：影响力10x10x10——大学生平等报告”，艾玛再次登台发表演讲，强调了大学生活对构建平等社会的重要性。美丽大方，双商极高，这样的女明星谁不爱？",
                    "https://pic2.zhimg.com/80/v2-2f71efbb891bf24f5f23c1ee3628d25e_720w.jpg?source=1940ef5c"));
            list.add(create(
                    "The Rivals in the Life生活中的对手",
                    "Jaguar is an endangered animal. It is said that there are less than 20 jaguars in the world currently, one of which is now living in the national zoo of Peru. In order to protect this jaguar, Peruvians singled out a pitch of land in the zoo for it, where there are herds of cattle, sheep and deer for the jaguar to eat. ",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606928622624&di=9be5215264657e8dfe9510f0ad4f0093&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_pic%2F16%2F12%2F05%2Fc068cca1cdb238baaf00d4a6c7b0d2f5.jpg"));
            list.add(create(
                    "The Life in Your Twenties 20岁的生活",
                    "On Constant Worrying 关于持续的担忧 Dufu says one of the greatest pieces of advice she ever received was about her constant worrying. “My mentor said, ‘If you would spend less time worrying about choices you don’t have and actually creating those choices, you would",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606928622615&di=e2ac852b15939f088592049ff8cc7f98&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F17%2F12%2F16%2F7153742bd0cbaeee2168a906c81a9a4f.jpg"));
            list.add(create(
                    "汇聚各种经典演讲的网站",
                    "American Rhetoric: The Power of Oratory in the United States",
                    "https://pic4.zhimg.com/80/v2-02346273f65be074e1fb44ec628d507b_720w.jpg?source=1940ef5c"));
        }


        return list;
    }
}
