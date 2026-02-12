package org.foodust.damageSkin.message;

import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum BaseMessage {

    PREFIX(""),
    PREFIX_C("[DamageSkin] "),

    INFO_RELOAD("리로드 되었습니다."),
    INFO_SET_SKIN("스킨이 설정 되었습니다."),
    INFO_SET_ALL_SKIN("모든 플레이어에게 스킨이 설정 되었습니다."),
    INFO_REMOVE_SKIN(" 님의 스킨이 해제 되었습니다."),

    COMMAND_DAMAGE_SKIN("데미지스킨"),
    COMMAND_SET("설정"),
    COMMAND_SET_ALL("전체설정"),
    COMMAND_REMOVE("제거"),
    COMMAND_RELOAD("리로드"),

    DEFAULT("기본"),

    ERROR("에러"),
    ERROR_NO_PLAYER("플레이어가 없습니다."),
    ERROR_NO_SKIN("스킨이 없습니다. : "),
    ERROR_WRONG_COMMAND("잘못 된 명령입니다."),
    ERROR_ALREADY_DELETE("이미 제거되었습니다. : "),
    ;

    private final String message;

    BaseMessage(String message) {
        this.message = message;
    }

    private static final Map<String, BaseMessage> commandInfo = new HashMap<>();

    static {
        for (BaseMessage baseMessage : EnumSet.range(COMMAND_SET, COMMAND_RELOAD)) {
            commandInfo.put(baseMessage.message, baseMessage);
        }
    }

    public static BaseMessage getByMessage(String message) {
        return commandInfo.getOrDefault(message, ERROR);
    }
}
