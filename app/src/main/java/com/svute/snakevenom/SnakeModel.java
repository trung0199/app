package com.svute.snakevenom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SnakeModel implements Serializable {
    private String garbageName;
    private int garbageImg;
    private String garbageInf;


    public SnakeModel(String garbageName, int garbageImg, String garbageInf) {
        this.garbageName = garbageName;
        this.garbageImg = garbageImg;
        this.garbageInf = garbageInf;
    }

    public String getGarbageName() {
        return garbageName;
    }

    public void setGarbageName(String garbageName) {
        this.garbageName = garbageName;
    }

    public int getGarbageImg() {
        return garbageImg;
    }

    public void setGarbageImg(int garbageImg) {
        this.garbageImg = garbageImg;
    }

    public String getGarbageInf() {
        return garbageInf;
    }

    public void setGarbageInf(String garbageInf) {
        this.garbageInf = garbageInf;
    }


    public static List<SnakeModel>getMock(){
        return new ArrayList<>(Arrays.asList(
            new SnakeModel("Pin - Batterry",R.drawable.betteries,"PIN là rác thải không tái chế được. Pin là rác thải có hại. Thủy ngân có trong một cục pin cũng có thể làm ô nhiễm 500 lít nước hoặc 1m khối đất trong vòng 50 năm... Khi con người hấp thụ qua đường ăn uống hoặc hít thở, các độc tố phát tán từ pin có thể gây hại não, thận, hệ thống sinh sản và tim mạch."),
            new SnakeModel("Rác thải hữu cơ - Biological Garbage",R.drawable.rac_thai_huu_co,"Rác hữu cơ là các loại rác dễ phân hủy như thức ăn thừa, rau củ quả, trái cây, bã trà, cà phê, cỏ, lá cây,… Những rác thải này sẽ được đem đi chế tạo thành phân bón."),
            new SnakeModel("Thủy tinh nâu - Brown glass",R.drawable.brown_glass,"Thủy tinh nâu là một loại rác tái chế được và được sử dụng đựng hóa chất, thuốc,...vì vậy việc xử lý loại rác thải này rất quan trọng."),
            new SnakeModel("Giấy carton - Cardboard",R.drawable.cardboard,"Thùng giấy carton có thể tái sử dụng thành các loại bao bì giấy tái chế khác nhau như thùng carton đóng hàng, hộp giấy carton, túi giấy, thiệp chúc, danh thiếp, quyển sổ, khăn giấy…. Chính vì thế, việc tái chế hay tận dụng thùng giấy cũng góp một phần cực kỳ quan trọng trong việc bảo vệ môi trường hiện nay."),
            new SnakeModel("Quần áo cũ - Old Clothes",R.drawable.clothes,"Tính trên phạm vi toàn cầu, ngành thời trang mỗi năm tạo ra tới 92 triệu tấn vải phế liệu, trong đó riêng nước Mỹ là 17 triệu tấn vải vóc, quần áo bị vứt bỏ."),
            new SnakeModel("Thủy tinh xanh - Green glass",R.drawable.green_glass,"Thủy tinh xanh thường được sử dụng như thủy tinh trắng nhưng giá thành đắt hơn, thường được dùng với mục đích trang trí. Nó dễ tái chế và mất ít thời gian."),
            new SnakeModel("Kim loại - Metal",R.drawable.kim_loai,"Rác thải kim loại thường thấy là các loại lon, vỏ hộp thức ăn,... hầu hết chúng rất dễ tái chế và thu gom. "),
            new SnakeModel("Giấy - Paper",R.drawable.giay,"Giấy là một trong những loại rác tái chế dễ, lượng giấy thải ra mỗi ngày trên thế giới là rất lớn và chúng sẽ được tái chế cho lần sử dụng tiếp theo để bảo vệ môi trường"),
            new SnakeModel("Nhựa - Plastic",R.drawable.nhua,"Nhựa là loại rác thải cực kỳ khó phân hủy chính vì thế chúng ta nên hạn chế sử dụng, tiết kiệm các chai,ly nhựa và tái chế chúng."),
            new SnakeModel("Giày cũ - Shoes",R.drawable.oldshoes,"Những đôi giày cũ là tổng hợp của cả hai loại rác thải nhựa và vải, cả hai loại rác thải này rất khó phân hủy vì vậy phải có biện pháp xử lý thích hợp."),
            new SnakeModel("Rác thải sinh hoạt -  Trash",R.drawable.racsinhhoat,"Là những rác thải như: tã, khẩu trang,... chúng liên quan đến vệ sinh và an toàn sức khỏe nên cần phải đeo khẩu trang và mang bao tay khi xử lý."),
            new SnakeModel("Thủy tinh trắng - White glass",R.drawable.white_glass,"Thủy tinh trắng là loại thủy tinh phổ biến nhất được sử dụng rộng rãi cho nhiều mục đích. Chúng dễ tái chế như thủy tinh xanh.")
        ));
    }

}
