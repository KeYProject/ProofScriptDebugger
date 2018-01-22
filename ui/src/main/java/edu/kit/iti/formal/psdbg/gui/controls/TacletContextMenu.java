package edu.kit.iti.formal.psdbg.gui.controls;

import de.uka.ilkd.key.control.ProofControl;
import de.uka.ilkd.key.gui.nodeviews.TacletMenu.TacletAppComparator;
import de.uka.ilkd.key.logic.Name;
import de.uka.ilkd.key.logic.PosInOccurrence;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.pp.AbbrevMap;
import de.uka.ilkd.key.pp.NotationInfo;
import de.uka.ilkd.key.pp.PosInSequent;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.rule.*;
import edu.kit.iti.formal.psdbg.gui.controller.DebuggerMain;
import edu.kit.iti.formal.psdbg.gui.controller.Events;
import edu.kit.iti.formal.psdbg.interpreter.KeYProofFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.key_project.util.collection.ImmutableList;
import org.key_project.util.collection.ImmutableSLList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copied from TacletMenu and adapted to JavaFX style. This is NOT a menu
 * anymore but a view controller. There is a field rootMenu to access the actual
 * menu.
 *
 * @author Victor Schuemmer
 * @author Alexander Weigl
 * @version 1.0
 * @see de.uka.ilkd.key.gui.nodeviews.TacletMenu
 */
public class TacletContextMenu extends ContextMenu {

    private static final Set<Name> CLUTTER_RULESETS = new LinkedHashSet<Name>();
    private static final Set<Name> CLUTTER_RULES = new LinkedHashSet<Name>();

    static {
        CLUTTER_RULESETS.add(new Name("notHumanReadable"));
        CLUTTER_RULESETS.add(new Name("obsolete"));
        CLUTTER_RULESETS.add(new Name("pullOutQuantifierAll"));
        CLUTTER_RULESETS.add(new Name("pullOutQuantifierEx"));
    }

    static {
        CLUTTER_RULES.add(new Name("cut_direct_r"));
        CLUTTER_RULES.add(new Name("cut_direct_l"));
        CLUTTER_RULES.add(new Name("case_distinction_r"));
        CLUTTER_RULES.add(new Name("case_distinction_l"));
        CLUTTER_RULES.add(new Name("local_cut"));
        CLUTTER_RULES.add(new Name("commute_and_2"));
        CLUTTER_RULES.add(new Name("commute_or_2"));
        CLUTTER_RULES.add(new Name("boxToDiamond"));
        CLUTTER_RULES.add(new Name("pullOut"));
        CLUTTER_RULES.add(new Name("typeStatic"));
        CLUTTER_RULES.add(new Name("less_is_total"));
        CLUTTER_RULES.add(new Name("less_zero_is_total"));
        CLUTTER_RULES.add(new Name("applyEqReverse"));

        // the following are used for drag'n'drop interactions
        CLUTTER_RULES.add(new Name("eqTermCut"));
        CLUTTER_RULES.add(new Name("instAll"));
        CLUTTER_RULES.add(new Name("instEx"));
    }

    private PosInSequent pos;

    @FXML
    private ContextMenu rootMenu;
    @FXML
    private MenuItem noRules;
    @FXML
    private Menu moreRules;
    @FXML
    private Menu insertHidden;
    @FXML
    private MenuItem copyToClipboard;
    @FXML
    private MenuItem createAbbr;
    @FXML
    private MenuItem enableAbbr;
    @FXML
    private MenuItem disableAbbr;
    @FXML
    private MenuItem changeAbbr;


    private Goal goal;
    private PosInOccurrence occ;
    private NotationInfo notationInfo = new NotationInfo();

    public TacletContextMenu(KeYProofFacade keYProofFacade, PosInSequent pos, Goal goal) {
        Utils.createWithFXML(this);

        /*mediator = getContext().getKeYMediator();
        comp = new TacletAppComparator();
        insertHiddenController.initViewController(getMainApp(), getContext());
    */

        if (pos == null)
            throw new IllegalArgumentException(
                    "Argument pos must not be null.");
        this.pos = pos;
        this.goal = goal;

        occ = pos.getPosInOccurrence();
        //MediatorProofControl c = new MediatorProofControl(new DefaultAbstractMediatorUserInterfaceControlAdapter());
        ProofControl c = DebuggerMain.FACADE.getEnvironment().getUi().getProofControl();
        final ImmutableList<BuiltInRule> builtInRules = c.getBuiltInRule(goal, occ);

        try {
            ImmutableList<TacletApp> findTaclet = c.getFindTaclet(goal, occ);

            createTacletMenu(
                    removeRewrites(findTaclet)
                            .prepend(c.getRewriteTaclet(goal, occ)),
                    c.getNoFindTaclet(goal), builtInRules);
        } catch (NullPointerException e) {

        }

        //proofMacroMenuController.initViewController(getMainApp(), getContext());
        //proofMacroMenuController.init(occ);
    }

    /**
     * Removes RewriteTaclet from the list.
     *
     * @param list the IList<Taclet> from where the RewriteTaclet are removed
     * @return list without RewriteTaclets
     */
    private static ImmutableList<TacletApp> removeRewrites(
            ImmutableList<TacletApp> list) {
        // return list;

        ImmutableList<TacletApp> result = ImmutableSLList.<TacletApp>nil();
        Iterator<TacletApp> it = list.iterator();

        while (it.hasNext()) {
            TacletApp tacletApp = it.next();
            Taclet taclet = tacletApp.taclet();
            result = (taclet instanceof RewriteTaclet ? result
                    : result.prepend(tacletApp));
        }
        return result;
    }

    /**
     * Sorts the TacletApps with the given TacletAppComparator.
     *
     * @param finds the list to sort (will not be changed)
     * @param comp  the comparator
     * @return the sorted list
     */
    public static ImmutableList<TacletApp> sort(ImmutableList<TacletApp> finds,
                                                TacletAppComparator comp) {
        ImmutableList<TacletApp> result = ImmutableSLList.<TacletApp>nil();

        List<TacletApp> list = new ArrayList<TacletApp>(finds.size());

        for (final TacletApp app : finds) {
            list.add(app);
        }

        Collections.sort(list, comp);

        for (final TacletApp app : list) {
            result = result.prepend(app);
        }

        return result;
    }

    /**
     * Creates the menu by adding all submenus and items.
     */
    private void createTacletMenu(ImmutableList<TacletApp> find,
                                  ImmutableList<TacletApp> noFind,
                                  ImmutableList<BuiltInRule> builtInList) {

        List<TacletApp> findTaclets = find.stream().collect(Collectors.toList());
        List<TacletApp> noFindTaclets = noFind.stream().collect(Collectors.toList());

        Collections.sort(findTaclets,
                //compare by name
                Comparator.comparing(o -> o.taclet().name())
        );

        List<TacletApp> toAdd = new ArrayList<>(findTaclets.size() + noFindTaclets.size());
        toAdd.addAll(findTaclets);

        boolean rulesAvailable = find.size() > 0;

        if (pos.isSequent()) {
            rulesAvailable |= noFind.size() > 0;
            toAdd.addAll(noFindTaclets);
        }

        if (rulesAvailable) {
            createMenuItems(toAdd);
        } else {
            noRules.setVisible(true);
        }

        if (occ != null)
            createAbbrevSection(pos.getPosInOccurrence().subTerm());

    }

    /**
     public TacletFilter getFilterForInteractiveProving() {
     if(this.filterForInteractiveProving == null) {
     this.filterForInteractiveProving = new TacletFilter() {
     protected boolean filter(Taclet taclet) {
     String[] var2 = JoinProcessor.SIMPLIFY_UPDATE;
     int var3 = var2.length;

     for(int var4 = 0; var4 < var3; ++var4) {
     String name = var2[var4];
     if(name.equals(taclet.name().toString())) {
     return false;
     }
     }

     return true;
     }
     };
     }

     return this.filterForInteractiveProving;
     }*/

    /**
     * Creates menu items for all given taclets. A submenu for insertion of
     * hidden terms will be created if there are any, and rare rules will be in
     * a 'More rules' submenu.
     *
     * @param taclets the list of taclets to create menu items for
     */
    private void createMenuItems(List<TacletApp> taclets) {
        int idx = 0;
        for (final TacletApp app : taclets) {
            final Taclet taclet = app.taclet();

       /*     if (!mediator.getFilterForInteractiveProving().filter(taclet)) {
                continue;
            }*/

            final MenuItem item = new MenuItem(app.taclet().name().toString());
            item.setOnAction(event -> {
                handleRuleApplication(app);
            });


            // Items for insertion of hidden terms appear in a submenu.
            /*if (insertHiddenController.isResponsible(item)) {
                insertHiddenController.addCell(item);
            } else */
            {
                // If one of the sets contains the rule it is considered rare
                // and moved to a 'More rules' submenu.
                boolean rareRule = false;
                for (RuleSet rs : taclet.getRuleSets()) {
                    if (CLUTTER_RULESETS.contains(rs.name()))
                        rareRule = true;
                }
                if (CLUTTER_RULES.contains(taclet.name()))
                    rareRule = true;

                if (rareRule)
                    moreRules.getItems().add(item);
                else
                    rootMenu.getItems().add(idx++, item);
            }
        }
        // Make submenus visible iff they are not empty.
        if (moreRules.getItems().size() > 0)
            moreRules.setVisible(true);
        /*if (!insertHiddenController.isEmpty())
            insertHiddenController.setVisible(true);
            */

    }

    /**
     * Manages the visibility of all menu items dealing with abbreviations based
     * on if the given term already is abbreviated and if the abbreviation is
     * enabled.
     *
     * @param t the term to check if there already is an abbreviation of
     */

    private void createAbbrevSection(Term t) {
        AbbrevMap scm = notationInfo.getAbbrevMap();
        if (scm.containsTerm(t)) {
            changeAbbr.setVisible(true);
            if (scm.isEnabled(t)) {
                disableAbbr.setVisible(true);
            } else {
                enableAbbr.setVisible(true);
            }
        } else {
            createAbbr.setVisible(true);
        }
    }

    /**
     * Handles the application of an 'ordinary' rule.
     *
     * @param event
     */
    private void handleRuleApplication(TacletApp event) {
        // Synchronization for StaticSequentView
     /*   parentController.setLastTacletActionID(parentController.getOwnID());
        mediator.getUI().getProofControl().selectedTaclet(
                ((TacletMenuItem) event.getSource()).getTaclet(), goal,
                pos.getPosInOccurrence());*/

        //System.out.println("event = [" + event + "]");

        Events.fire(new Events.TacletApplicationEvent(event, pos.getPosInOccurrence(), goal));
    }

    /**
     * Handles rule application for automode.
     *
     * @param event
     */
    @FXML
    private void handleFocussedRuleApplication(ActionEvent event) {
        // Synchronization for StaticSequentView
        // parentController.setLastTacletActionID(parentController.getOwnID());
        //mediator.getUI().getProofControl()
//                .startFocussedAutoMode(pos.getPosInOccurrence(), goal);
    }

    /**
     * Handles action of the 'Copy to clipboard' menu item.
     *
     * @param event
     */
    @FXML
    private void handleCopyToClipboard(ActionEvent event) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        //content.putString(parentController.getProofString()
        //        .substring(pos.getBounds().start(), pos.getBounds().end()));
        clipboard.setContent(content);
    }

    /**
     * Checks whether a string is a valid term abbreviation (is not empty and
     * only contains 0-9, a-z, A-Z and underscore (_)).
     *
     * @param s the string to check
     * @return true iff the string is a valid term abbreviation
     */
    private boolean validateAbbreviation(String s) {
        if (s == null || s.length() == 0)
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (!((s.charAt(i) <= '9' && s.charAt(i) >= '0')
                    || (s.charAt(i) <= 'z' && s.charAt(i) >= 'a')
                    || (s.charAt(i) <= 'Z' && s.charAt(i) >= 'A')
                    || s.charAt(i) == '_'))
                return false;
        }
        return true;
    }

    /**
     * Handles the creation of a term abbreviation.
     *
     * @param event
     */
    @FXML
    private void handleCreateAbbreviation(ActionEvent event) {
        if (occ.posInTerm() != null) {
            final String oldTerm = occ.subTerm().toString();
            final String term = oldTerm.length() > 200
                    ? oldTerm.substring(0, 200) : oldTerm;
            abbreviationDialog("Create Abbreviation",
                    "Enter abbreviation for term: \n" + term + "\n", null);
        }
    }

    /**
     * Handles the change of a term abbreviation.
     *
     * @param event
     */
    @FXML
    private void handleChangeAbbreviation(ActionEvent event) {
        if (occ.posInTerm() != null) {
            abbreviationDialog("Change Abbreviation",
                    "Enter abbreviation for term: \n"
                            + occ.subTerm().toString(),
                    notationInfo.getAbbrevMap().getAbbrev(occ.subTerm()).substring(1));
        }
    }

    /**
     * Opens a dialog that requires the input of a term abbreviation and iff a
     * valid abbreviation is given applies it.
     *
     * @param header    the header text for the dialog
     * @param message   the message of the dialog
     * @param inputText preset text for the input line. Can be used to pass an already
     *                  existing abbreviation to change.
     */
    private void abbreviationDialog(String header, String message,
                                    String inputText) {
        TextInputDialog dialog = new TextInputDialog(inputText);

        // Get the Stage and addCell KeY Icon.
       /* Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons()
                .addCell(new Image(NUIConstants.KEY_APPLICATION_WINDOW_ICON_PATH));
        dialog.setTitle("Abbreviation Dialog");
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(abbreviation -> {
            if (abbreviation != null) {
                if (!validateAbbreviation(abbreviation)) {
                    getMainApp().showAlert("Sorry", null,
                            "Only letters, numbers and '_' are allowed for Abbreviations",
                            AlertType.INFORMATION);
                } else {
                    try {
                        AbbrevMap abbrevMap = mediator.getNotationInfo()
                                .getAbbrevMap();
                        if (abbrevMap.containsTerm(occ.subTerm()))
                            abbrevMap.changeAbbrev(occ.subTerm(), abbreviation);
                        else
                            abbrevMap.put(occ.subTerm(), abbreviation, true);
                        parentController.forceRefresh();
                    } catch (Exception e) {
                        getMainApp().showAlert("Sorry",
                                "Something has gone wrong.", e.getMessage(),
                                AlertType.ERROR);
                    }
                }
            }
        });*/
    }

    /**
     * Handles the enable of a term abbreviation.
     *
     * @param event
     */
    @FXML
    private void handleEnableAbbreviation(ActionEvent event) {
        if (occ.posInTerm() != null) {
            notationInfo.getAbbrevMap().setEnabled(occ.subTerm(), true);
            //   parentController.forceRefresh();
        }
    }

    /**
     * Handles the disable of a term abbreviation.
     *
     * @param event
     */
    @FXML
    private void handleDisableAbbreviation(ActionEvent event) {
        if (occ.posInTerm() != null) {
            notationInfo.getAbbrevMap().setEnabled(occ.subTerm(), false);
            //  parentController.forceRefresh();
        }
    }
}
