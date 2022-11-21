package com.review.controllers;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.review.models.*;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;


public class PrimaryController implements Initializable {
    private Client client ;

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    private String check;

    public List<Product> getTiki() {
        return Tiki;
    }

    public void setTiki(List<Product> tiki) {
        Tiki = tiki;
    }

    private List<Product> Tiki = new ArrayList<>();
    private List<Product> Shopee = new ArrayList<>();

    public List<Product> getSendo() {
        return Sendo;
    }

    public void setSendo(List<Product> sendo) {
        Sendo = sendo;
    }

    private List<Product> Sendo = new ArrayList<>() ;
    private List<Product> Lazada = new ArrayList<>();

    @FXML
    private BorderPane container;

    public ItemListController getItemListController() {
        return itemListController;
    }

    public void setItemListController(ItemListController itemListController) {
        this.itemListController = itemListController;
    }

    private ItemListController itemListController;
    private ItemDetailController itemDetailController;
    private InfoDetailController infoDetailController;
    private RatingAggregatorController ratingAggregatorController;

    @FXML
    private Label rating_aggregator_button;

    @FXML
    private Label search_product_button;

    public TextField getSearch_product() {
        return search_product;
    }

    @FXML
    private TextField search_product ;

    public void setInfoDetailController(InfoDetailController infoDetailController) {
        this.infoDetailController = infoDetailController;
    }

    public InfoDetailController getInfoDetailController() {
        return infoDetailController;
    }
    public ItemDetailController getItemDetailController() {
        return itemDetailController;
    }

    public void setItemDetailController(ItemDetailController itemDetailController) {
        this.itemDetailController = itemDetailController;
    }

    @FXML
    void search_enter(ActionEvent event)throws IOException,ClassNotFoundException {
        client.SearchProduct(search_product.getText());
         Tiki = new ArrayList<>();
         Sendo = new ArrayList<>();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/review/item_list.fxml"));
        fxmlLoader.load();
        itemListController = fxmlLoader.getController();
        itemListController.productList = client.ReceiveList();
        swapItemList();
    }

    @FXML
    void rating_aggregator_press(MouseEvent event) throws IOException, ClassNotFoundException {
        this.rating_aggregator_button.getStyleClass().remove("action");
        this.search_product_button.getStyleClass().remove("action");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/review/rating_aggregator.fxml"));
        fxmlLoader.load();
        client.GetReviewProduct(184061913);
        ratingAggregatorController.rateList = client.ReceiveListReviews();

        swapRatingAggregator();
        this.rating_aggregator_button.getStyleClass().add("action");
    }

    @FXML
    void search_product_press(MouseEvent event) {
        this.rating_aggregator_button.getStyleClass().remove("action");
        this.search_product_button.getStyleClass().remove("action");

        swapItemList();
        this.search_product_button.getStyleClass().add("action");
    }

    public void setContainer(Pane newPane){
        container.setCenter(newPane);
    }

    public void swapItemList(){
        itemListController.openItemList(this);
    }
    public void swapRatingAggregator(){
        try {
            ratingAggregatorController.openRatingAggregator(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void swapItemDetail(Product product, ProductDetail productDetail){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/review/item_detail.fxml"));
            fxmlLoader.load();
            itemDetailController = fxmlLoader.getController();
            itemDetailController.openItemDetail(this);
            itemDetailController.product_name_label.setText(product.getProductName());
            itemDetailController.product_price_label.setText("VNĐ"+product.getPrice());
            itemDetailController.product_sale_price_label.setText("VNĐ"+product.getPrice_sale());
            String[] images ;
            images = productDetail.getImagesUrl();
            String deteleWebp1 = product.getImageUrl().replace(".webp","");
                if(images.length>=3) {
                    String deteleWebp2 = images[1].replace(".webp","");
                    String deteleWebp3 = images[2].replace(".webp","");
                        Image image = new Image(deteleWebp1);
                        Image image2 = new Image(deteleWebp2);
                        Image image3 = new Image(deteleWebp3);
                        itemDetailController.product_image_1.setImage(image);
                        itemDetailController.product_image_2.setImage(image2);
                        itemDetailController.product_image_3.setImage(image3);
                }
                else {
                    Image image = new Image(deteleWebp1);
                    itemDetailController.product_image_1.setImage(image);
                }
            //info_detail
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/review/info_detail.fxml"));
            Pane newpane = fxmlLoader.load();
            itemDetailController.infoDetailController = fxmlLoader.getController();
            itemDetailController.infoDetailController.Info_detail.setText(productDetail.getDescription());
            itemDetailController.rating_list.getChildren().addAll(newpane);
            itemDetailController.info_pane = newpane;
            //rating_list_detail
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/review/rating_list.fxml"));

            Pane newpane1 = fxmlLoader.load();
            itemDetailController.ratingListController = fxmlLoader.getController();

            client.GetReviewProduct(product.getproductID());
            itemDetailController.ratingListController.rateList = client.ReceiveListReviews();

            itemDetailController.ratingListController.openRatingList(this);

            itemDetailController.ratingListController.rating_detail_product.setText(String.valueOf(productDetail.getRating_average()));
            itemDetailController.ratingListController.count_rating_product.setText(String.valueOf(productDetail.getReview_count())+" Đánh giá");

            itemDetailController.rating_pane = newpane1;
        }catch (IOException | ClassNotFoundException e){

        }
    }
    public void disconnect() throws IOException, ClassNotFoundException {
        client.disconnect();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            try {
                client=new Client();
                client.ConnectClient();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/review/item_list.fxml"));
                fxmlLoader.load();
                itemListController = fxmlLoader.getController();

                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/review/rating_aggregator.fxml"));
                fxmlLoader.load();
                ratingAggregatorController = fxmlLoader.getController();

                client.SearchProduct("ipad");
                itemListController.productList = client.ReceiveList();
                search_product.setText("ipad");
                setCheck("tiki");

//                client.GetReviewProduct(184061913);
//                ratingAggregatorController.rateList = client.ReceiveListReviews();

//                swapRatingAggregator();
                swapItemList();

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
    }
    public void SetClient(Client cl){
        this.client=client;
    }
    public Client getClient(){
        return client;
    }

}
