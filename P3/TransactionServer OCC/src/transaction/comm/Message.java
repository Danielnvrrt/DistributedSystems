/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package transaction.comm;

/**
 *
 * @author Usuario
 */
public class Message implements MessageTypes{
    // type of message, types are defined in interface MessageTypes
    int type;
    // content that is specific to a certain message type
    Object content;

    
    // constructor
    public Message(int type, Object content) {
        this.type = type;
        this.content = content;
    }
    
    // getters
    public int getType() 
    {
        return type;
    }

    public Object getContent()
    {
        return content;
    }
}
