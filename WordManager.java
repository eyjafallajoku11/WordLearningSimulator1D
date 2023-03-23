import java.util.ArrayList;
import java.util.List;

public class WordManager {
    private List<List<String>> words_dataset = new ArrayList<>();
    private final int wordCount;
    public WordManager(String[] words) {
        wordCount = words.length;
        for (short i = 0; i < 5; i++) {
            if (i == 2){
                words_dataset.add(new ArrayList<>(List.of(words)));
            }
            else words_dataset.add(new ArrayList<>());
        }
    }
    public String getWord(){
        short n = 0 ;
        int count = 0;
        for (short i = 0; i<4;i++){
            n=i;
            int t = words_dataset.get(i).size();
            count+=t;
            if (Math.random()<0.5 & t!=0 | count == wordCount){
                break;
            }
        }
        int m = (int) (Math.random() * words_dataset.get(n).size());
        return words_dataset.get(n).get(m);
    }
    @Override
    public String toString() {
        return "WordManager{" +
                "words_dataset=" + words_dataset +
                '}';
    }
}
