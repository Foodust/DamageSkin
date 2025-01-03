package org.foodust.damageSkin.Message;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum BaseMessage {

    // prefix
    PREFIX(""),
    PREFIX_C("<gradient:yellow:blue><bold>[DamageSkin]</bold></gradient> "),

    // info
    INFO_RELOAD("<blue>리로드</blue> 되었습니다."),
    INFO_SET_SKIN("<aqua>스킨</aqua>이 설정 되었습니다."),
    INFO_REMOVE_SKIN(" 님의 <aqua>스킨</aqua>이 해제 되었습니다."),

    // command
    COMMAND_DAMAGE_SKIN("데미지스킨"),
    COMMAND_SET("설정"),
    COMMAND_REMOVE("제거"),
    COMMAND_RELOAD("리로드"),
    // 기본
    DEFAULT("기본"),
        // Error
    ERROR("에러"),
    ERROR_NO_PLAYER("<red>플레이어</red>가 없습니다."),
    ERROR_NO_SKIN("<red>스킨</red>이 없습니다. : "),
    ERROR_WRONG_COMMAND("<dark_red>잘못 된 명령입니다.</dark_red>"),
    ERROR_ALREADY_DELETE("<dark_red>이미 제거되었습니다. : </dark_red>"),
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
        return commandInfo.getOrDefault(message,ERROR);
    }
}
