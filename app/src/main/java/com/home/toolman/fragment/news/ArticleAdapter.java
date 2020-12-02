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
            list.add(create("美电视台记者因误报有关弗林新闻被停职四周",
                    "【环球网报道】据俄罗斯卫星网12月3日报道，美国ABC电视台记者布莱恩·罗素因在有关美国总统前国家安全顾问迈克尔·弗林的新闻报道中的失误，临时被停职。",
                    "http://cms-bucket.nosdn.127.net/5d18566fde70407b9cc3a728822115c320171203133214.jpeg?imageView&thumbnail=550x0"));
            list.add(create("英语杂货店",
                    "\"你行你上啊\"不要再说成\"You can you up!\"啦，正确的表达是...put up or shut up",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606927719716&di=d7f5049b40a10b16386b0c868d655a7a&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20151028%2Fmp38301942_1446013674271_6.jpeg"));
        } else if (p == 1) {
            list.add(create(
                    "2019年投产 电咖整车生产基地落户浙江绍兴",
                    "网易汽车11月30日报道 两周前的广州车展上，电咖发布了其首款电动汽车EV10，官方指导价为13.38万-14.18万，扣除补贴后的零售价为5.98万元-6.78万元，性价比很高。抛开车辆本身，引起业界关注的是这家新势力造车企业的几位核心成员，当年上汽大众团队的三位老兵--张海亮、向东平、牛胜福携手用了957天造了一辆可以上市的车。",
                    "http://cms-bucket.nosdn.127.net/674c392123254bb69bdd9227442965eb20171129203658.jpeg?imageView&thumbnail=550x0"));
            list.add(create(
                    "2017年进入尾声，苹果大笔押注的ARkit还好么？",
                    "谷歌推出了AR眼镜、ARCore平台和应用在手机上的Project Tango，Facebook也上线了AR开发平台和工具。至于苹果，更是把AR当做发展的重中之重。在新品iPhone8和iPhoneX中，后置摄像头专为AR进行校准，前置摄像头还添加了可以带来更好AR效果的深度传感器。",
                    "http://cms-bucket.nosdn.127.net/catchpic/7/76/76135ac5d3107a1d5ba11a8ee2fc7e27.jpg?imageView&thumbnail=550x0"));
            list.add(create(
                    "亚马逊CTO：我们要让人类成为机器人的中心！",
                    "那些相信应用下载会让世界变得更美好的智能手机布道者和应用爱好者们，会在AWS re:Invent大会上感到不自在。亚马逊网络服务首席技术官Werner Vogels表示，所有这些都未能实现信息的民主化。",
                    "http://cms-bucket.nosdn.127.net/ddb758f16a7d4aa3aa422ec385fc3e5020171204081818.jpeg?imageView&thumbnail=550x0"));
            list.add(create(
                    "有特斯拉车主想用免费的充电桩挖矿，但这可能行不通",
                    "在社交网络 Facebook 上的一个特斯拉车主群组中，有人开脑洞说可以尝试自己组装矿机放在特斯拉后备箱中，接入车载电池的电源，然后将车停到超级充电桩附近，就能用免费获得的电力挖矿了。",
                    "http://crawl.nosdn.127.net/nbotreplaceimg/4ce9c743e6c02f6777d22278e2ef8bc3/2b33e32532db204fe207693c82719660.jpg"));
        } else if (p == 2) {
            list.add(create(
                    "2017年进入尾声，苹果大笔押注的ARkit还好么？",
                    "谷歌推出了AR眼镜、ARCore平台和应用在手机上的Project Tango，Facebook也上线了AR开发平台和工具。至于苹果，更是把AR当做发展的重中之重。在新品iPhone8和iPhoneX中，后置摄像头专为AR进行校准，前置摄像头还添加了可以带来更好AR效果的深度传感器。",
                    "http://cms-bucket.nosdn.127.net/catchpic/7/76/76135ac5d3107a1d5ba11a8ee2fc7e27.jpg?imageView&thumbnail=550x0"));
            list.add(create(
                    "亚马逊CTO：我们要让人类成为机器人的中心！",
                    "那些相信应用下载会让世界变得更美好的智能手机布道者和应用爱好者们，会在AWS re:Invent大会上感到不自在。亚马逊网络服务首席技术官Werner Vogels表示，所有这些都未能实现信息的民主化。",
                    "http://cms-bucket.nosdn.127.net/ddb758f16a7d4aa3aa422ec385fc3e5020171204081818.jpeg?imageView&thumbnail=550x0"));
            list.add(create("中企投资巴西获支持 英媒:巴西人感激\"保住饭碗\"",
                    "参考消息网12月4日报道 英媒称，里约热内卢附近的阿苏港曾被埃克·巴蒂斯塔称为“通往中国的公路”，10多年前，这位现已名誉扫地的巴西前首富创建了这个超级港，大宗商品热潮结束后，他在巴西的商业帝国几乎无一幸存并于2014年破产，但此后至今有一个项目仍蓬勃发展，那就是阿苏港。",
                    "http://cms-bucket.nosdn.127.net/catchpic/8/8b/8ba2d19b7f63efc5cf714960d5edd2c3.jpg?imageView&thumbnail=550x0"));
            list.add(create("美电视台记者因误报有关弗林新闻被停职四周",
                    "【环球网报道】据俄罗斯卫星网12月3日报道，美国ABC电视台记者布莱恩·罗素因在有关美国总统前国家安全顾问迈克尔·弗林的新闻报道中的失误，临时被停职。",
                    "http://cms-bucket.nosdn.127.net/5d18566fde70407b9cc3a728822115c320171203133214.jpeg?imageView&thumbnail=550x0"));
            list.add(create(
                    "2019年投产 电咖整车生产基地落户浙江绍兴",
                    "网易汽车11月30日报道 两周前的广州车展上，电咖发布了其首款电动汽车EV10，官方指导价为13.38万-14.18万，扣除补贴后的零售价为5.98万元-6.78万元，性价比很高。抛开车辆本身，引起业界关注的是这家新势力造车企业的几位核心成员，当年上汽大众团队的三位老兵--张海亮、向东平、牛胜福携手用了957天造了一辆可以上市的车。",
                    "http://cms-bucket.nosdn.127.net/674c392123254bb69bdd9227442965eb20171129203658.jpeg?imageView&thumbnail=550x0"));
        }


        return list;
    }
}
