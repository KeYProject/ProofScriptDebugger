package edu.kit.formal.gui.controls;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.kit.formal.gui.model.Breakpoint;
import edu.kit.formal.gui.model.MainScriptIdentifier;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ast.ASTNode;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * A controller for manageing the open script files in the dock nodes.
 *
 * @author Sarah Grebing
 */
public class ScriptController {
    public static final String LINE_HIGHLIGHT_POSTMORTEM = "line-highlight-postmortem";
    private static Logger logger = LogManager.getLogger(ScriptController.class);
    private final DockPane parent;
    private ObjectProperty<MainScriptIdentifier> mainScript = new SimpleObjectProperty<>();
    private final ObservableMap<ScriptArea, DockNode> openScripts = FXCollections.observableMap(new HashMap<>());
    private ScriptArea lastScriptArea;
    private ASTNodeHighlighter postMortemHighlighter = new ASTNodeHighlighter(LINE_HIGHLIGHT_POSTMORTEM);


    public ScriptController(DockPane parent) {
        this.parent = parent;
    }

    public ObservableMap<ScriptArea, DockNode> getOpenScripts() {
        return openScripts;
    }

    public DockNode getDockNode(File filepath) {
        return getDockNode(findEditor(filepath));
    }

    private DockNode getDockNode(ScriptArea editor) {
        if (editor == null) {
            return null;
        }
        return openScripts.get(editor);
    }

    public ScriptArea createNewTab(File filePath) {
        filePath = filePath.getAbsoluteFile();
        if (findEditor(filePath) == null) {
            ScriptArea area = new ScriptArea();
            area.mainScriptProperty().bindBidirectional(mainScript);
            area.setFilePath(filePath);
            DockNode dockNode = createDockNode(area);
            openScripts.put(area, dockNode);
            dockNode.dock(parent, DockPos.LEFT);

            return area;
        } else {
            logger.info("File already exists. Will not load it again");
            ScriptArea area = findEditor(filePath);
            return area;
        }
    }

    private DockNode createDockNode(ScriptArea area) {
        DockNode dockNode = new DockNode(area, area.getFilePath().getName(), new MaterialDesignIconView(MaterialDesignIcon.FILE_DOCUMENT));
        dockNode.closedProperty().addListener(o -> {
            openScripts.remove(area);
        });
        area.filePathProperty().addListener((observable, oldValue, newValue) -> dockNode.setTitle(newValue.getName()));

        if (lastScriptArea == null)
            dockNode.dock(parent, DockPos.LEFT);
        else
            dockNode.dock(parent, DockPos.CENTER, getDockNode(lastScriptArea));

        area.dirtyProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                    dockNode.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.FILE_DOCUMENT));
                else
                    dockNode.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.FILE_DOCUMENT_BOX));
            }
        });

        this.lastScriptArea = area;
        area.focusedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("area = [" + area + "]");
            if (newValue)
                lastScriptArea = area;
        });

        return dockNode;
    }


    public Set<Breakpoint> getBreakpoints() {
        HashSet<Breakpoint> breakpoints = new HashSet<>();
        openScripts.keySet().forEach(tab ->
                breakpoints.addAll(tab.getBreakpoints())
        );
        return breakpoints;
    }

    public ScriptArea findEditor(File filePath) {
        return openScripts.keySet().stream()
                .filter(scriptArea ->
                        scriptArea.getFilePath().equals(filePath)
                )
                .findAny()
                .orElse(null);
    }


    public void saveCurrentScriptAs(File scriptFile) throws IOException {
        for (ScriptArea area : openScripts.keySet()) {
            if (area.isFocused()) {
                FileUtils.write(scriptFile, area.getText(), Charset.defaultCharset());
                area.setFilePath(scriptFile);
                area.setDirty(false);
            }
        }
    }

    public List<ProofScript> getCombinedAST() {
        ArrayList<ProofScript> all = new ArrayList<>();
        for (ScriptArea area : openScripts.keySet()) {
            all.addAll(Facade.getAST(area.getText()));
        }
        return all;
    }

    private String[] ADJECTIVES =
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

    private String[] ANIMALS =
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

    public String getRandomName() {
        Random r = new Random();
        return (ADJECTIVES[r.nextInt(ADJECTIVES.length)] + "_" + ANIMALS[r.nextInt(ANIMALS.length)] + ".kps").toLowerCase();
    }

    public ScriptArea newScript() {
        ScriptArea area = new ScriptArea();
        area.setFilePath(new File(getRandomName()));
        openScripts.put(area, createDockNode(area));
        return area;
    }

    public ASTNodeHighlighter getPostMortemHighlighter() {
        return postMortemHighlighter;
    }

    public class ASTNodeHighlighter {
        public final String clazzName;
        private ScriptArea.RegionStyle lastRegion;
        private ScriptArea lastScriptArea;

        private ASTNodeHighlighter(String clazzName) {
            this.clazzName = clazzName;
        }

        public void remove() {
            lastScriptArea.getMarkedRegions().remove(lastRegion);
        }

        public void highlight(ASTNode node) {
            remove();
            ScriptArea.RegionStyle r = asRegion(node);
            ScriptArea area = findEditor(node);
            area.getMarkedRegions().add(r);

            getDockNode(area).focus();
            area.requestFocus();

            lastScriptArea = area;
            lastRegion = r;
        }

        private ScriptArea.RegionStyle asRegion(ASTNode node) {
            return new ScriptArea.RegionStyle(node.getRuleContext().getStart().getStartIndex(),
                    node.getRuleContext().getStop().getStopIndex(), clazzName);

        }
    }

    private ScriptArea findEditor(ASTNode node) {
        File f = new File(node.getRuleContext().getStart().getInputStream().getSourceName());
        return findEditor(f);
    }
}
