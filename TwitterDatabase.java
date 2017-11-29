import java.util.*;

/**
 * Created by Yu Fang on 27/11/2015.
 */
public class TwitterDatabase {
    private Map<String, TwitterUser> name2User= new HashMap<String, TwitterUser>();
    private Map<Tweet, TwitterUser> tweet2User= new HashMap<Tweet, TwitterUser>();
    private Map<String, Set<Tweet>> word2Tweet= new HashMap<String, Set<Tweet>>();
    private Map<TwitterUser, Set<Tweet>> user2Tweet= new HashMap<TwitterUser, Set<Tweet>>();

    public static void main(String[] args) {
        TwitterDatabase database=new TwitterDatabase("uofmtweets.dat");

        System.out.println("-- Part 1 --");
        System.out.printf("name table size=  %d\n", database.getNameTable().size());
        System.out.printf("tweet table size=  %d\n",database.getTweetTable().size());
        System.out.printf("word table size=  %d\n",database.getWordTable().size());
        System.out.printf("user table size=  %d\n", database.getUserTable().size());
        System.out.println();

        System.out.println("-- Part 2 --");
        List tweetCount=database.getTweetCounts();
        List wordCount=database.getWordCounts();
        List minnesotaUsage=database.getWordUsage("minnesota");
        List universityUsage=database.getWordUsage("university");
        System.out.println();

        System.out.println("Top 10 results of getTweetCounts");
        for (int i=0; i<10; i++){
            System.out.println(tweetCount.get(i));
        }
        System.out.println();

        System.out.println("Top 10 results of getWordCounts");
        for (int i=0; i<10; i++){
            System.out.println(wordCount.get(i));
        }
        System.out.println();

        System.out.println("Top 10 results of getWordUsage(\"minnesota\")");
        for (int i=0; i<10; i++){
            System.out.println(minnesotaUsage.get(i));
        }
        System.out.println();

        System.out.println("Top 10 results of getWordUsage(\"university\")");
        for (int i=0; i<10; i++){
            System.out.println(universityUsage.get(i));
        }
        System.out.println();

        System.out.println("--  Part 3 --");
        System.out.println("HashMap");
        TwitterDatabase HashMap= new TwitterDatabase("uofmtweets.dat");
        List tweetCount_H=HashMap.getTweetCounts();
        List wordCount_H=HashMap.getWordCounts();
        List minnesotaUsage_H=HashMap.getWordUsage("minnesota");
        List universityUsage_H=HashMap.getWordUsage("university");
        System.out.println(tweetCount.get(4));
        System.out.println(wordCount.get(7));
        System.out.println(minnesotaUsage.get(2));


        System.out.println("\nTreeMap");
        TwitterDatabase TreeMap= new TwitterDatabase("uofmtweets.dat",
                new TreeMap<String, TwitterUser>(),new TreeMap<Tweet, TwitterUser>(),
                new TreeMap<String, Set<Tweet>>(), new TreeMap<TwitterUser,Set<Tweet>>());
        List tweetCount_T=TreeMap.getTweetCounts();
        List wordCount_T= TreeMap.getWordCounts();
        List minnesotaUsage_T=TreeMap.getWordUsage("minnesota");
        List universityUsage_T=TreeMap.getWordUsage("university");
        System.out.println(tweetCount.get(4));
        System.out.println(wordCount.get(7));
        System.out.println(minnesotaUsage.get(2));

        System.out.println("\nLinearScanMap");
        TwitterDatabase LinearMap= new TwitterDatabase("uofmtweets.dat",
                new LinearScanMap<String, TwitterUser>(), new LinearScanMap<Tweet, TwitterUser>(),
                new LinearScanMap<String, Set<Tweet>>(), new LinearScanMap<TwitterUser,Set<Tweet>>());
        List tweetCount_L= LinearMap.getTweetCounts();
        List wordCount_L= LinearMap.getWordCounts();
        List minnesotaUsage_L= LinearMap.getWordUsage("minnesota");
        List universityUsage_L= LinearMap.getWordUsage("university");
        System.out.println(tweetCount.get(4));
        System.out.println(wordCount.get(7));
        System.out.println(minnesotaUsage.get(2));
    }







    public TwitterUser addOrGetUser(String name){
        if (name2User.containsKey(name)){
            return name2User.get(name);
        }else{
            TwitterUser tUser= new TwitterUser(name);
            this.name2User.put(name, tUser);
            this.user2Tweet.put(tUser,new HashSet<Tweet>());
            return tUser;
        }
    }

    public int addWord(String word, Tweet tweet){
        if (word2Tweet.containsKey(word)){
            this.word2Tweet.get(word).add(tweet);
            return word2Tweet.get(word).size();
        }else{
            this.word2Tweet.put(word, new HashSet<>());
            this.word2Tweet.get(word).add(tweet);
            return 1;
        }
    }

    public Tweet addTweet(String msg, TwitterUser user){
        Tweet newTweet= new Tweet(msg);
        this.tweet2User.put(newTweet, user);
        if(user2Tweet.get(user) !=null) {
           /* Set tweets = this.user2Tweet.get(user);
            tweets.add(newTweet);
            this.user2Tweet.put(user,tweets);*/
            this.user2Tweet.get(user).add(newTweet);
        }

        Set<String> wordSet=newTweet.getWords();
        for (Iterator<String> it= wordSet.iterator(); it.hasNext();){
            String word= it.next();
            this.addWord(word, newTweet);
        }

        return newTweet;
    }

    public Map<String, TwitterUser> getNameTable(){
        return name2User;
    }

    public Map<Tweet, TwitterUser> getTweetTable(){
        return tweet2User;
    }

    public Map<String, Set<Tweet>> getWordTable(){
        return word2Tweet;
    }

    public Map<TwitterUser, Set<Tweet>> getUserTable(){
        return user2Tweet;
    }


    public TwitterDatabase(String datfile){
        TweetReader reader=new TweetReader(datfile);
        while(reader.advance()){
            String curID= reader.getTweeterID();
            String curTweet=reader.getTweet();

            TwitterUser thisUser=this.addOrGetUser(curID);
            this.addTweet(curTweet,thisUser);
        }
    }

    public TwitterDatabase(String datfile, Map<String, TwitterUser> name2User,
                           Map<Tweet, TwitterUser> tweet2User,
                           Map<String, Set<Tweet>> word2Tweet,
                           Map<TwitterUser, Set<Tweet>> user2Tweet){
        this.name2User=name2User;
        this.tweet2User=tweet2User;
        this.word2Tweet=word2Tweet;
        this.user2Tweet=user2Tweet;

        TweetReader reader=new TweetReader(datfile);
        while(reader.advance()){
            String curID= reader.getTweeterID();
            String curTweet=reader.getTweet();

            TwitterUser thisUser=this.addOrGetUser(curID);
            this.addTweet(curTweet,thisUser);
        }

    }








    public List<ItemCount> getTweetCounts() {
        long start= System.currentTimeMillis();

        List<ItemCount> TweetCountList = new ArrayList<>();

        Iterator it = user2Tweet.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry data = (Map.Entry) it.next();
            TwitterUser user = (TwitterUser) data.getKey();
            HashSet Tweets = (HashSet) data.getValue();

            ItemCount newItem = new ItemCount(user, Tweets.size());
            TweetCountList.add(newItem);
        }

        Collections.sort(TweetCountList);

        long duration= System.currentTimeMillis()-start;
        System.out.printf("getTweetCounts() took %dms to execute\n", duration);

        return TweetCountList;
    }

    public List<ItemCount> getWordCounts(){
        long start= System.currentTimeMillis();

        List<ItemCount> WordCountList= new ArrayList<>();

        Iterator it= word2Tweet.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry data = (Map.Entry) it.next();
            String word= (String) data.getKey();
            HashSet Tweets= (HashSet) data.getValue();

            ItemCount newItem= new ItemCount(word, Tweets.size());
            WordCountList.add(newItem);
        }

        Collections.sort(WordCountList);

        long duration=System.currentTimeMillis()-start;
        System.out.printf("getWordCounts() took %dms to execute\n", duration);

        return WordCountList;
    }

    public List<ItemCount> getWordUsage(String word){
        long start= System.currentTimeMillis();

        List<ItemCount> WordUsageList= new ArrayList<>();
        word = word.toLowerCase();
        Iterator it= user2Tweet.entrySet().iterator();


        for(TwitterUser tweetUser:user2Tweet.keySet()) {
            int countOfUser=0;
            Set<Tweet> userTweets = user2Tweet.get(tweetUser);
            for (Tweet theTweet : word2Tweet.get(word)) {
                if(userTweets.contains(theTweet))
                {
                    countOfUser++;
                }
            }
            ItemCount newItem= new ItemCount(tweetUser, countOfUser);
            WordUsageList.add(newItem);

        }

        Collections.sort(WordUsageList);

        long duration= System.currentTimeMillis()-start;
        System.out.printf("getWordUsage(String word) took %dms to execute\n",duration);

        return WordUsageList;
    }

}
