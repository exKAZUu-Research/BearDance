package net.exkazuu.mimicdance;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.interpreter.ActionType;
import net.exkazuu.mimicdance.interpreter.BodyPartType;
import net.exkazuu.mimicdance.interpreter.CharacterType;

import java.util.Collection;

public class CharacterSprite {
    private final CharacterType charaType;
    private final ImageView[] bodyParts;
    private final int[][] firstImageIds;
    private final int[][] secondImageIds;

    private CharacterSprite(CharacterType charaType, View view) {
        this.charaType = charaType;

        BodyPartType[] bodyPartTypes = BodyPartType.values();
        this.bodyParts = new ImageView[bodyPartTypes.length];

        ActionType[] actions = ActionType.values();
        CharacterType[] characters = CharacterType.values();
        this.firstImageIds = new int[actions.length][characters.length];
        this.secondImageIds = new int[actions.length][characters.length];

        initializeImageViews(charaType, view, bodyPartTypes);
        initializeImageIds(view, actions, characters);
        renderInitialState();
    }

    private void initializeImageIds(View view, ActionType[] actions, CharacterType[] characters) {
        String packageName = view.getContext().getPackageName();
        for (int i = 0; i < actions.length; i++) {
            ActionType action = actions[i];
            for (int j = 0; j < characters.length; j++) {
                String prefix = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, characters[j].name()) + "_";
                String actionName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, action.name());
                String firstName = prefix + actionName.replace("down", "up") + "2";
                String secondName = prefix + actionName.replace("up", "up3").replace("down", "up1").replace("jump", "basic");
                firstImageIds[i][j] = view.getResources().getIdentifier(firstName, "drawable", packageName);
                secondImageIds[i][j] = view.getResources().getIdentifier(secondName, "drawable", packageName);
            }
        }
    }

    private void initializeImageViews(CharacterType charaType, View view, BodyPartType[] bodyPartTypes) {
        String packageName = view.getContext().getPackageName();
        for (int i = 0; i < bodyPartTypes.length; i++) {
            String idString = "character" + bodyPartTypes[i].name();
            String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, charaType.name()) + bodyPartTypes[i].name();
            int id = view.getResources().getIdentifier(idString, "id", packageName);
            bodyParts[i] = (ImageView) view.findViewById(id);
            if (i < bodyPartTypes.length - 1) {
                String drawableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name) + "_up1";
                int drawableId = view.getResources().getIdentifier(drawableName, "drawable", packageName);
                bodyParts[i].setImageResource(drawableId);
                bodyParts[i].setVisibility(View.VISIBLE);
            }
        }
    }

    public static CharacterSprite createPiyoLeft(View view) {
        return new CharacterSprite(CharacterType.Piyo, view);
    }

    public static CharacterSprite createPiyoRight(View view) {
        return new CharacterSprite(CharacterType.AltPiyo, view);
    }

    public static CharacterSprite createCoccoLeft(View view) {
        return new CharacterSprite(CharacterType.Cocco, view);
    }

    public static CharacterSprite createCoccoRight(View view) {
        return new CharacterSprite(CharacterType.AltCocco, view);
    }

    public void renderInitialState() {
        renderCompleteState(Lists.newArrayList(ActionType.LeftFootDown, ActionType.Jump.LeftHandDown,
            ActionType.RightFootDown, ActionType.RightHandDown));
        switch(charaType) {
            case Piyo:
                getBody().setImageResource(R.drawable.piyo_basic);
                break;
            case AltPiyo:
                getBody().setImageResource(R.drawable.alt_piyo_basic);
                break;
            case Cocco:
                getBody().setImageResource(R.drawable.cocco_basic);
                break;
            case AltCocco:
                getBody().setImageResource(R.drawable.alt_cocco_basic);
                break;
        }
    }

    public void renderIntermediateState(Collection<ActionType> actions) {
        for (ActionType action : actions) {
            bodyParts[action.toBodyPart().ordinal()]
                .setImageResource(firstImageIds[action.ordinal()][charaType.ordinal()]);
        }
        if (actions.contains(ActionType.Jump)) {
            for (int i = 0; i < bodyParts.length - 1; i++) {
                bodyParts[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    public void renderCompleteState(Collection<ActionType> actions) {
        for (ActionType actionType : actions) {
            bodyParts[actionType.toBodyPart().ordinal()]
                .setImageResource(secondImageIds[actionType.ordinal()][charaType.ordinal()]);
        }
        if (actions.contains(ActionType.Jump)) {
            for (int i = 0; i < bodyParts.length - 1; i++) {
                bodyParts[i].setVisibility(View.VISIBLE);
            }
        }
    }

    public void renderIntermediateErrorState() {
        if (charaType == CharacterType.Piyo) {
            getBody().setImageResource(R.drawable.korobu_1);
        } else {
            getBody().setImageResource(R.drawable.alt_korobu_1);
        }
        for (int i = 0; i < bodyParts.length - 1; i++) {
            bodyParts[i].setVisibility(View.INVISIBLE);
        }
    }

    public void renderCompleteErrorState() {
        if (charaType == CharacterType.Piyo) {
            getBody().setImageResource(R.drawable.korobu_3);
        } else {
            getBody().setImageResource(R.drawable.alt_korobu_3);
        }
    }

    public ImageView getBody() {
        return bodyParts[4];
    }
}
