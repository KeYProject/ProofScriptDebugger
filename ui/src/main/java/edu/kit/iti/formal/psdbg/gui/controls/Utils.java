package edu.kit.iti.formal.psdbg.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.IProgramMethod;
import de.uka.ilkd.key.pp.LogicPrinter;
import de.uka.ilkd.key.pp.NotationInfo;
import de.uka.ilkd.key.pp.ProgramPrinter;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.speclang.Contract;
import edu.kit.iti.formal.psdbg.interpreter.data.GoalNode;
import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.parser.ScriptLanguageLexer;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

/**
 * @author Alexander Weigl
 * @version 1 (05.06.17)
 */
public class Utils {
    private static Logger logger = LogManager.getLogger(Utils.class);

    private static String[] ADJECTIVES =
            ("abandoned,able,absolute,adorable,adventurous,academic,acceptable,acclaimed,accomplished,accurate,aching,acidic,acrobatic,active,actual,adept,admirable,admired," +
                    "adolescent,adorable,adored,advanced,afraid,affectionate,aged,aggravating,aggressive,agile,agitated,agonizing,agreeable,ajar,alarmed,alarming,alert," +
                    "alienated,alive,all,altruistic,amazing,ambitious,ample,amused,amusing,anchored,ancient,angelic,angry,anguished,animated,annual,another,antique,anxious," +
                    "any,apprehensive,appropriate,apt,arctic,arid,aromatic,artistic,ashamed,assured,astonishing,athletic,attached,attentive,attractive,austere,authentic,authorized," +
                    "automatic,avaricious,average,aware,awesome,awful,awkward,babyish,bad,back,baggy,bare,barren,basic,beautiful,belated,beloved,beneficial,better,best,bewitched," +
                    "big,big-hearted,biodegradable,bite-sized,bitter,black,black-and-white,bland,blank,blaring,bleak,blind,blissful,blond,blue,blushing,bogus,boiling,bold,bony," +
                    "boring,bossy,both,bouncy,bountiful,bowed,brave,breakable,brief,bright,brilliant,brisk,broken,bronze,brown,bruised,bubbly,bulky,bumpy,buoyant,burdensome,burly," +
                    "bustling,busy,buttery,buzzing,calculating,calm,candid,canine,capital,carefree,careful,careless,caring,cautious,cavernous,celebrated,charming,cheap,cheerful," +
                    "cheery,chief,chilly,chubby,circular,classic,clean,clear,clear-cut,clever,close,closed,cloudy,clueless,clumsy,cluttered,coarse,cold,colorful,colorless," +
                    "colossal,comfortable,common,compassionate,competent,complete,complex,complicated,composed,concerned,concrete,confused,conscious,considerate,constant,content," +
                    "conventional,cooked,cool,cooperative,coordinated,corny,corrupt,costly,courageous,courteous,crafty,crazy,creamy,creative,creepy,criminal,crisp,critical," +
                    "crooked,crowded,cruel,crushing,cuddly,cultivated,cultured,cumbersome,curly,curvy,cute,cylindrical,damaged,damp,dangerous,dapper,daring,darling,dark,dazzling" +
                    ",dead,deadly,deafening,dear,dearest,decent,decimal,decisive,deep,defenseless,defensive,defiant,deficient,definite,definitive,delayed,delectable,delicious," +
                    "delightful,delirious,demanding,dense,dental,dependable,dependent,descriptive,deserted,detailed,determined,devoted,different,difficult,digital,diligent,dim," +
                    "dimpled,dimwitted,direct,disastrous,discrete,disfigured,disgusting,disloyal,dismal,distant,downright,dreary,dirty,disguised,dishonest,dismal,distant,distinct," +
                    "distorted,dizzy,dopey,doting,double,downright,drab,drafty,dramatic,dreary,droopy,dry,dual,dull,dutiful,each,eager,earnest,early,easy,easy-going,ecstatic,edible" +
                    ",educated,elaborate,elastic,elated,elderly,electric,elegant,elementary,elliptical,embarrassed,embellished,eminent,emotional,empty,enchanted,enchanting,energetic," +
                    "enlightened,enormous,enraged,entire,envious,equal,equatorial,essential,esteemed,ethical,euphoric,even,evergreen,everlasting,every,evil,exalted,excellent,exemplary" +
                    ",exhausted,excitable,excited,exciting,exotic,expensive,experienced,expert,extraneous,extroverted,extra-large,extra-small,fabulous,failing,faint,fair,faithful,fake" +
                    ",false,familiar,famous,fancy,fantastic,far,faraway,far-flung,far-off,fast,fat,fatal,fatherly,favorable,favorite,fearful,fearless,feisty,feline,female,feminine,few," +
                    "fickle,filthy,fine,finished,firm,first,firsthand,fitting,fixed,flaky,flamboyant,flashy,flat,flawed,flawless,flickering,flimsy,flippant,flowery,fluffy,fluid," +
                    "flustered,focused,fond,foolhardy,foolish,forceful,forked,formal,forsaken,forthright,fortunate,fragrant,frail,frank,frayed,free,French,fresh,frequent,friendly," +
                    "frightened,frightening,frigid,frilly,frizzy,frivolous,front,frosty,frozen,frugal,fruitful,full,fumbling,functional,funny,fussy,fuzzy,gargantuan,gaseous,general" +
                    ",generous,gentle,genuine,giant,giddy,gigantic,gifted,giving,glamorous,glaring,glass,gleaming,gleeful,glistening,glittering,gloomy,glorious,glossy,glum,golden," +
                    "good,good-natured,gorgeous,graceful,gracious,grand,grandiose,granular,grateful,grave,gray,great,greedy,green,gregarious,grim,grimy,gripping,grizzled,gross" +
                    ",grotesque,grouchy,grounded,growing,growling,grown,grubby,gruesome,grumpy,guilty,gullible,gummy,hairy,half,handmade,handsome,handy,happy,happy-go-lucky,hard," +
                    "harmful,harmless,harmonious,harsh,hasty,hateful,haunting,healthy,heartfelt,hearty,heavenly,heavy,hefty,helpful,helpless,hidden,hideous,high,high-level,hilarious," +
                    "hoarse,hollow,homely,honest,honorable,honored,hopeful,horrible,hospitable,hot,huge,humble,humiliating,humming,humongous,hungry,hurtful,husky,icky,icy,ideal," +
                    "idealistic,identical,idle,idiotic,idolized,ignorant,ill,illegal,ill-fated,ill-informed,illiterate,illustrious,imaginary,imaginative,immaculate,immaterial,immediate" +
                    ",immense,impassioned,impeccable,impartial,imperfect,imperturbable,impish,impolite,important,impossible,impractical,impressionable,impressive,improbable,impure" +
                    ",inborn,incomparable,incompatible,incomplete,inconsequential,incredible,indelible,inexperienced,indolent,infamous,infantile,infatuated,inferior,infinite,informal" +
                    ",innocent,insecure,insidious,insignificant,insistent,instructive,insubstantial,intelligent,intent,intentional,interesting,internal,international,intrepid,ironclad" +
                    ",irresponsible,irritating,itchy,jaded,jagged,jam-packed,jaunty,jealous,jittery,joint,jolly,jovial,joyful,joyous,jubilant,judicious,juicy,jumbo,junior,jumpy,juvenile," +
                    "kaleidoscopic,keen,key,kind,kindhearted,kindly,klutzy,knobby,knotty,knowledgeable,knowing,known,kooky,kosher,lame,lanky,large,last,lasting,late,lavish,lawful,lazy,leading," +
                    "lean,leafy,left,legal,legitimate,light,lighthearted,likable,likely,limited,limp,limping,linear,lined,liquid,little,live,lively,livid,loathsome,lone,lonely,long," +
                    "loose,lopsided,lost,loud,lovable,lovely,loving,low,loyal,lucky,lumbering,luminous,lumpy,lustrous,luxurious,mad,made-up,magnificent,majestic,major,male,mammoth,married," +
                    "marvelous,masculine,massive,mature,meager,mealy,mean,measly,meaty,medical,mediocre,medium,meek,mellow,melodic,memorable,menacing,merry,messy,metallic,mild,milky,mindless," +
                    "miniature,minor,minty,miserable,miserly,misguided,misty,mixed,modern,modest,moist,monstrous,monthly,monumental,moral,mortified,motherly,motionless,mountainous,muddy,muffled," +
                    "multicolored,mundane,murky,mushy,musty,muted,mysterious,naive,narrow,nasty,natural,naughty,nautical,near,neat,necessary,needy,negative,neglected,negligible,neighboring," +
                    "nervous,new,next,nice,nifty,nimble,nippy,nocturnal,noisy,nonstop,normal,notable,noted,noteworthy,novel,noxious,numb,nutritious,nutty,obedient,obese,oblong,oily,oblong," +
                    "obvious,occasional,odd,oddball,offbeat,offensive,official,old,old-fashioned,only,open,optimal,optimistic,opulent,orange,orderly,organic,ornate,ornery,ordinary,original,other," +
                    "our,outlying,outgoing,outlandish,outrageous,outstanding,oval,overcooked,overdue,overjoyed,overlooked,palatable,pale,paltry,parallel,parched,partial,passionate,past,pastel," +
                    "peaceful,peppery,perfect,perfumed,periodic,perky,personal,pertinent,pesky,pessimistic,petty,phony,physical,piercing,pink,pitiful,plain,plaintive,plastic,playful,pleasant," +
                    "pleased,pleasing,plump,plush,polished,polite,political,pointed,pointless,poised,poor,popular,portly,posh,positive,possible,potable,powerful,powerless,practical,precious," +
                    "present,prestigious,pretty,precious,previous,pricey,prickly,primary,prime,pristine,private,prize,probable,productive,profitable,profuse,proper,proud,prudent,punctual,pungent," +
                    "puny,pure,purple,pushy,putrid,puzzled,puzzling,quaint,qualified,quarrelsome,quarterly,queasy,querulous,questionable,quick,quick-witted,quiet,quintessential,quirky,quixotic," +
                    "quizzical,radiant,ragged,rapid,rare,rash,raw,recent,reckless,rectangular,ready,real,realistic,reasonable,red,reflecting,regal,regular,reliable,relieved,remarkable,remorseful," +
                    "remote,repentant,required,respectful,responsible,repulsive,revolving,rewarding,rich,rigid,right,ringed,ripe,roasted,robust,rosy,rotating,rotten,rough,round,rowdy,royal,rubbery," +
                    "rundown,ruddy,rude,runny,rural,rusty,sad,safe,salty,same,sandy,sane,sarcastic,sardonic,satisfied,scaly,scarce,scared,scary,scented,scholarly,scientific,scornful,scratchy,scrawny," +
                    "second,secondary,second-hand,secret,self-assured,self-reliant,selfish,sentimental,separate,serene,serious,serpentine,several,severe,shabby,shadowy,shady,shallow,shameful,shameless," +
                    "sharp,shimmering,shiny,shocked,shocking,shoddy,short,short-term,showy,shrill,shy,sick,silent,silky,silly,silver,similar,simple,simplistic,sinful,single,sizzling,skeletal,skinny," +
                    "sleepy,slight,slim,slimy,slippery,slow,slushy,small,smart,smoggy,smooth,smug,snappy,snarling,sneaky,sniveling,snoopy,sociable,soft,soggy,solid,somber,some,spherical,sophisticated" +
                    ",sore,sorrowful,soulful,soupy,sour,Spanish,sparkling,sparse,specific,spectacular,speedy,spicy,spiffy,spirited,spiteful,splendid,spotless,spotted,spry,square,squeaky," +
                    "squiggly,stable,staid,stained,stale,standard,starchy,stark,starry,steep,sticky,stiff,stimulating,stingy,stormy,straight,strange,steel,strict,strident,striking,striped,strong," +
                    "studious,stunning,stupendous,stupid,sturdy,stylish,subdued,submissive,substantial,subtle,suburban,sudden,sugary,sunny,super,superb,superficial,superior,supportive,sure-footed" +
                    ",surprised,suspicious,svelte,sweaty,sweet,sweltering,swift,sympathetic,tall,talkative,tame,tan,tangible,tart,tasty,tattered,taut,tedious,teeming,tempting,tender,tense,tepid," +
                    "terrible,terrific,testy,thankful,that,these,thick,thin,third,thirsty,this,thorough,thorny,those,thoughtful,threadbare,thrifty,thunderous,tidy,tight,timely,tinted,tiny,tired,torn" +
                    ",total,tough,traumatic,treasured,tremendous,tragic,trained,tremendous,triangular,tricky,trifling,trim,trivial,troubled,true,trusting,trustworthy,trusty,truthful,tubby,turbulent" +
                    ",twin,ugly,ultimate,unacceptable,unaware,uncomfortable,uncommon,unconscious,understated,unequaled,uneven,unfinished,unfit,unfolded,unfortunate,unhappy,unhealthy,uniform," +
                    "unimportant,unique,united,unkempt,unknown,unlawful,unlined,unlucky,unnatural,unpleasant,unrealistic,unripe,unruly,unselfish,unsightly,unsteady,unsung,untidy,untimely,untried," +
                    "untrue,unused,unusual,unwelcome,unwieldy,unwilling,unwitting,unwritten,upbeat,upright,upset,urban,usable,used,useful,useless,utilized,utter,vacant,vague,vain,valid,valuable," +
                    "vapid,variable,vast,velvety,venerated,vengeful,verifiable,vibrant,vicious,victorious,vigilant,vigorous,villainous,violet,violent,virtual,virtuous,visible,vital,vivacious,vivid," +
                    "voluminous,wan,warlike,warm,warmhearted,warped,wary,wasteful,watchful,waterlogged,watery,wavy,wealthy,weak,weary,webbed,wee,weekly,weepy,weighty,weird,welcome,well-documented" +
                    ",well-groomed,well-informed,well-lit,well-made,well-off,well-to-do,well-worn,wet,which,whimsical,whirlwind,whispered,white,whole,whopping,wicked,wide,wide-eyed,wiggly,wild," +
                    "willing,wilted,winding,windy,winged,wiry,wise,witty,wobbly,woeful,wonderful,wooden,woozy,wordy,worldly,worn,worried,worrisome," +
                    "worse,worst,worthless,worthwhile,worthy,wrathful,wretched,writhing,wrong,wry,yawning,yearly,yellow,yellowish,young,youthful,yummy,zany,zealous,zesty,zigzag").split(",");
    private static String[] ANIMALS =
            ("Cat,Caterpillar,Cattle,Chamois,Cheetah,Chicken,Chimpanzee,Chinchilla,Chough,Coati,Cobra,Cockroach,Cod,Cormorant," +
                    "Coyote,Crab,Crane,Crocodile,Crow,Curlew,Deer,Dinosaur,,Dog,,Dogfish,Shark,Dolphin,Donkey," +
                    "Dotterel,Dove,Dragonfly,Duck,,Mallard,Dugong,Dunlin,Eagle,Echidna,Eel,Eland,Elephant,Elephant,seal,Elk," +
                    "Emu,Falcon,Ferret,Finch,Fish,,Flamingo,Fly,Fox,Frog,Gaur,Gazelle,Gerbil,Giant,panda,Giraffe,Gnat,Gnu,Goat" +
                    ",Goldfinch,Goosander,Goose,Gorilla,Goshawk,Grasshopper,Grouse,Guanaco,Guinea,fowl,Guinea,pig,Gull,Hamster,Hare,Hawk,Hedgehog," +
                    "Heron,Herring,Hippo,Hornet,Horse,,Hummingbird,Hyena,Ibex,Ibis,Jackal,Jaguar,Jay,Jellyfish,Kangaroo,Kinkajou,Koala,Komodo,dragon," +
                    "Kouprey,Kudu,Lapwing,Lark,Lemur,Leopard,Lion,Llama,Lobster,Locust,,Loris,Louse,Lyrebird,Magpie,Mallard,Duck,Mammoth,Manatee,Mandrill,Mink," +
                    "Mole,Mongoose,Monkey,Moose,Mouse,Mosquito,Narwhal,Newt,Nightingale,Octopus,Okapi,Opossum,Ostrich,Otter,Owl,Oyster,Panther,Parrot,Panda," +
                    "Partridge,Peafowl,Pelican,Penguin,Pheasant,Pig,Boar,Pigeon,,Polarbear,Horse,Porcupine,Porpoise,Prairie dog," +
                    "Quail,Quelea,Quetzal,Rabbit,,Raccoon,Ram,Sheep,Rat,,Raven,Red,deer,Red,panda,Reindeer,(caribou),Rhinoceros,Rook,Salamander,Salmon,Sandpiper," +
                    "Sardine,Sea,lion,Sea,urchin,Seahorse,Seal,Shark,Sheep,,Ram,Shrew,Skunk,Sloth,Snail,Snake,,Spider,,Squirrel,Starling,Stegosaurus,Swan,Tapir," +
                    "Tarsier,Termite,Tiger,Toad,Turkey,,Turtle,Vicuña,Wallaby,Walrus,Wasp,Water,buffalo,Weasel,Whale,Wolf,Wolverine,Wombat,Wren,Yak,Zebra").split(",");

    private Utils() {
    }

    public static String getRandomName(String suffx) {
        return getRandomName() + suffx;
    }

    public static String getRandomName() {
        Random r = new Random();
        return (ADJECTIVES[r.nextInt(ADJECTIVES.length)] + "_" + ANIMALS[r.nextInt(ANIMALS.length)]).toLowerCase();
    }

    public static void createWithFXML(Object node) {
        URL resource = node.getClass().getResource(node.getClass().getSimpleName() + ".fxml");
        if (resource == null) {
            throw new IllegalArgumentException("Could not find FXML resource: " + node.getClass().getSimpleName() + ".fxml");
        }

        FXMLLoader loader = new FXMLLoader(resource);
        loader.setController(node);
        loader.setRoot(node);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Could not load fxml", e);
        }
    }

    public static String getJavaCode(Contract c) {
        IProgramMethod method = (IProgramMethod) c.getTarget();
        StringWriter writer = new StringWriter();
        ProgramPrinter pp = new ProgramPrinter(writer);
        try {
            pp.printFullMethodSignature(method);
            pp.printStatementBlock(method.getBody());
            writer.flush();
        } catch (IOException e) {
            logger.catching(e);
        }
        return writer.toString();
    }

    public static void showExceptionDialog(String title, String headerText, String contentText, Throwable ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.setWidth(400);
        alert.setHeight(400);
        alert.getDialogPane().setExpandableContent(expContent);

        alert.setHeight(600);
        alert.setWidth(400);

        alert.showAndWait();
    }

    public static void showWarningDialog(String title, String headerText, String contentText, Throwable ex) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);



        alert.setWidth(400);
        alert.setHeight(400);


        alert.setHeight(600);
        alert.setWidth(400);

        alert.showAndWait();
    }

    public static void showInfoDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);



        alert.setWidth(400);
        alert.setHeight(400);


        alert.setHeight(600);
        alert.setWidth(400);

        alert.showAndWait();
    }

    public static void addDebugListener(Property<?> property) {
        property.addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {
            String simpleName = property.getBean() != null ? property.getBean().getClass().getSimpleName() : "<n/a>";
            logger.debug("Property '{}' of '{}' changed from {} to {}",
                    property.getName(), simpleName, oldValue, newValue);
        });
    }

    public static <T> void addDebugListener(Property<T> property, java.util.function.Function<T, String> conv) {
        property.addListener((observable, oldValue, newValue) -> {
            String simpleName = property.getBean() != null ? property.getBean().getClass().getSimpleName() : "<n/a>";
            logger.debug("Property '{}' of '{}' changed from {} to {}", property.getName(),
                    simpleName,
                    oldValue == null ? "<null>" : conv.apply(oldValue),
                    newValue == null ? "<null>" : conv.apply(newValue));
        });
    }

    public static void addDebugListener(ObservableValue<?> o, String id) {
        o.addListener((ChangeListener<Object>) (observable, oldValue, newValue) ->
                logger.debug("Observable {} changed from {} to {}", id, oldValue, newValue));
    }

    public static Token findToken(String text, int insertionIndex) {
        ScriptLanguageLexer lexer = new ScriptLanguageLexer(CharStreams.fromString(text));
        for (Token t : lexer.getAllTokens()) {
            if (t.getType() == ScriptLanguageLexer.WS
                    || t.getType() == ScriptLanguageLexer.MULTI_LINE_COMMENT
                    || t.getType() == ScriptLanguageLexer.SINGLE_LINE_COMMENT)
                continue;

            if (t.getStopIndex() >= insertionIndex)
                return t;
        }
        return null;
    }

    public static String reprKeyDataList(ObservableList<GoalNode<KeyData>> kd) {
        return kd.stream()
                .map(Utils::reprKeyData)
                .reduce((a, b) -> a + b)
                .orElse("<no goal nodes>");
    }

    public static <T> String reprKeyData(GoalNode<KeyData> t) {
        return String.valueOf(t.getData().getNode().serialNr());
    }

    public static void showClosedProofDialog(String scriptName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Proof Closed");
        alert.setHeaderText("The proof is closed");
        alert.setContentText("The proof using " + scriptName + " is closed");
        alert.setWidth(500);
        alert.setHeight(400);
        alert.setResizable(true);

        alert.showAndWait();
    }

    public static void showOpenProofNotificationDialog(int noOfGoals) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Interpreter Finished Successfully");
        dialog.setHeaderText("Interactive Mode Possible");
        dialog.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.HAND_POINTING_RIGHT, "24.0"));

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Interpreter finished successfully"), 0, 0);
        String msg = String.format("%s %d %s ", "There were still", noOfGoals,
                "open goals.");
        String msg2 = "You can continue the proof interactively by using the interactive button.\nThis enables to point and click onto the sequents and apply rules";
        grid.add(new Label(msg), 0, 1);
        grid.add(new MaterialDesignIconView(MaterialDesignIcon.HAND_POINTING_RIGHT, "24.0"), 3, 1);

        //dialog.getDialogPane().setContent(grid);
        Label ta = new Label(msg2);
        ta.setWrapText(true);
        Pane p = new VBox(grid, ta);
        dialog.getDialogPane().setContent(p);
        dialog.showAndWait();

    }



    public static void intoClipboard(String s) {
        Map<DataFormat, Object> map = Collections.singletonMap(DataFormat.PLAIN_TEXT, s);
        Clipboard.getSystemClipboard().setContent(map);
        logger.info("Clipboard "+s);
    }

    /**
     * Prints a KeY-parsable String representation of a term
     * @param term to print
     * @param goal containing namespaces
     * @return
     */
    public static String printParsableTerm(Term term, Goal goal){
        Services services = goal.proof().getInitConfig().getServices();
        return printParsableTerm(term, services);
    }

    /**
     * Prints a KeY-parsable String representation of a term
     * @param term to print
     * @param services object containing namespaces
     * @return
     */
    public static String printParsableTerm(Term term, Services services) {

        NotationInfo ni = new NotationInfo();
        LogicPrinter p = new LogicPrinter(new ProgramPrinter(), ni, services);
        ni.refresh(services, false, false);
        String termString = "";
        try {
            p.printTerm(term);
        } catch (IOException ioe) {
            // t.toString();
        }
        termString = p.toString();
        return termString;
    }
}
