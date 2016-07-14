package net.exkazuu.mimicdance.interpreter;

public class Pose {
    private final boolean[] bodyPart2up;

    public Pose() {
        bodyPart2up = new boolean[BodyPartType.values().length - 1];
    }

    public boolean validate(Iterable<ActionType> actions, int characterNumber) {
        for (ActionType actionType : actions) {
            if (actionType == ActionType.Jump) {
                for (boolean up : bodyPart2up) {
                    if (up) {
                        return false;
                    }
                }
            } else if (actionType == ActionType.Touch) {
                boolean isValid = characterNumber == 0 ? isLeftHandUp() : isRightHandUp();
                if (!isValid) {
                    return false;
                }
            } else if (actionType.isUp() == bodyPart2up[actionType.toBodyPart().ordinal()]) {
                return false;
            }
        }
        return true;
    }

    public void change(Iterable<ActionType> actions) {
        for (ActionType actionType : actions) {
            if (actionType.toBodyPart() != BodyPartType.Body) {
                bodyPart2up[actionType.toBodyPart().ordinal()] = actionType.isUp();
            }
        }
    }

    public void reset() {
        for (int i = 0; i < bodyPart2up.length; i++) {
            bodyPart2up[i] = false;
        }
    }

    public boolean isLeftHandUp() {
        return bodyPart2up[BodyPartType.LeftHand.ordinal()];
    }

    public boolean isRightHandUp() {
        return bodyPart2up[BodyPartType.RightHand.ordinal()];
    }

    public boolean isLeftFootUp() {
        return bodyPart2up[BodyPartType.LeftFoot.ordinal()];
    }

    public boolean isRightFootUp() {
        return bodyPart2up[BodyPartType.RightFoot.ordinal()];
    }
}
