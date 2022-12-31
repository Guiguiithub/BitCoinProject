package ch.hevs.ag.Controller;

import ch.hevs.ag.Model.Transaction;
import ch.hevs.ag.ServiceData.BlockchainData;
import ch.hevs.ag.ServiceData.WalletData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class MainWindowController {

    //get access to the table in the UI with all the columns inside it
    @FXML
    public TableView<Transaction> tableView = new TableView<>() ;
    @FXML
    private TableColumn<Transaction, String> from ;
    @FXML
    private TableColumn<Transaction, String> to;
    @FXML
    private TableColumn<Transaction, Integer> value;
    @FXML
    private TableColumn<Transaction, String> timeStamp;
    @FXML
    private TableColumn<Transaction, String> signature;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField eCoins ;
    @FXML
    private TextArea publicKey ;

    //initialize all the value that you can get in the window
    public void initialize()
    {
        Base64.Encoder encoder = Base64.getEncoder() ;

        from.setCellValueFactory(new PropertyValueFactory<>("fromFX"));
        to.setCellValueFactory(new PropertyValueFactory<>("toFX"));
        value.setCellValueFactory(new PropertyValueFactory<>("valueFX"));
        timeStamp.setCellValueFactory(new PropertyValueFactory<>("timeStampFX"));
        signature.setCellValueFactory(new PropertyValueFactory<>("signatureFX"));

        //get the balance of the wallet
        eCoins.setText(BlockchainData.getInstance().getWalletBallanceFX());
        publicKey.setText(encoder.encodeToString(WalletData.getInstance().getWallet().getPublicKey().getEncoded()));

        //Fill the tableView in the UI
        tableView.setItems(BlockchainData.getInstance().getTransactionLedgerFX());
        //The cursor will be on the first line (last transaction)
        tableView.getSelectionModel().select(0);
    }

    public void toNewTransactionController()
    {
        Dialog<ButtonType> newTransactionController = new Dialog<>();
        newTransactionController.initOwner(borderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../View/AddNewTransaction.fxml"));
        try {
            newTransactionController.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Can't load");
            e.printStackTrace();
            return;
        }
        newTransactionController.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        Optional<ButtonType> result = newTransactionController.showAndWait();
        if (result.isPresent())
        {
            tableView.setItems(BlockchainData.getInstance().getTransactionLedgerFX());
            eCoins.setText(BlockchainData.getInstance().getWalletBallanceFX());
        }
    }
    @FXML
    public void refresh()
    {
        tableView.setItems(BlockchainData.getInstance().getTransactionLedgerFX());
        tableView.getSelectionModel().select(0);
        eCoins.setText(BlockchainData.getInstance().getWalletBallanceFX());
    }

    @FXML
    public void handleExit()
    {
        BlockchainData.getInstance().setExit(true);
        Platform.exit();
    }
}
