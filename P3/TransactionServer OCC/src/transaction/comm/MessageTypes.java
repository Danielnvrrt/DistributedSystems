/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package transaction.comm;

/**
 *
 * @author Usuario
 */
public interface MessageTypes {
    public static final int READ_REQUEST         = 1;
    public static final int READ_REQUEST_RESPONSE        = 2;
    public static final int WRITE_REQUEST         = 3;
    public static final int TRANSACTION_COMMITTED     = 4;
    public static final int TRANSACTION_ABORTED = 5;
    public static final int CLOSE_TRANSACTION = 6;
    public static final int OPEN_TRANSACTION = 7;
}
