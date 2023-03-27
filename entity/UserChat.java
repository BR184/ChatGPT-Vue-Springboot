package entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserChat)表实体类
 *
 * @author makejava
 * @since 2023-03-27 11:26:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChat implements Serializable{
    //用户ID
    private Long userid;
    //聊天ID
    private Long chatid;
    //文本随机性(0-2)
    private Float temperature;
    //文本多样性(0-1)
    private Float topP;
    //内容重复度(0-1)
    private Float presencePenalty;
    //字词重复度(0-1)
    private Float frequencyPenalty;
}

