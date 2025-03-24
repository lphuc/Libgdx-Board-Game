Game name: Dead Turn Online
thể loại: casual, chest, snowball effect
aha moment: hiệu ứng snowball, trò chơi gia tăng sự hồi hộp khi gần về cuối, tạo cảm giác phấn khích cho user khi nhặt được vật phẩm có lợi
key tech point: hiệu animation bắt mắt khi mở ô
concept art: đồ hoạ có tông màu tươi sáng đậm
deadline: Thời gian hoàn thành MVP tối đa 1 tháng

1. người chơi có thể customize nhân vật của mình đầu game
2. ván đầu tiên có thể mặc định cho user đấu với bot để làm quen
3. màn hình game chính là 1 bàn cờ gồm 40 ô 5x8
4. user khi mới vào game sẽ được đặt ở 1 ô ngẫu nhiên
5. mỗi 1 ván cờ (room) có tối thiểu 2 người chơi
6. trò chơi sẽ tự động add bot nếu user đó tạo room sau 10s mà ko có user khác
7. trò chơi có thể bắt đầu nếu có từ 2 user trở lên và tối đa là 4 user
8. user có thể lựa chọn đấu với người chơi hoặc với bot
9. user khi mới vào game sẽ được ngẫu nhiên vào 1 trong các ô trống bất kì trên bàn cờ
10. mỗi user sẽ có 1 character đại diện với chỉ 3 chỉ số HP = 100, Attack = 10, Armor = 0
11. mỗi 1 ô trên bàn cờ sẽ ẩn chứa ngẫu nhiên 1 trong các vật thể sau:
    - equipment tăng giáp
    - vũ khí tăng or giảm sát thương
    - đồ ăn tăng máu
    - thuốc độc gây mất máu
    - quái, người chơi phải chiến đấu với quái
12. người chơi ko thể nhìn thấy người chơi khác đang ở ô nào
13. khi người chơi khác nhặt được vật phẩm gì thì sẽ thông báo cho tất cả người chơi cùng phòng
14. người chơi di chuyển bằng cách drag ô nhân vật mình đang đứng tới ô bất kì
    - khi drag những ô người chơi có thể drag vào sẽ hiện màu xanh
15. người chơi chỉ có thể đi mỗi lượt 1 ô
16. nếu người chơi đi trúng ô có người chơi khác thì trận đấu tự động (turn-based) sẽ được bắt đầu
    - màn hình chiến đấu thô sơ sẽ hiện ra 2 character đứng đối diện nhau và đánh theo lượt
    - người chơi bị giết chết sẽ bị loại khỏi bàn cờ
    - người chơi chủ của ô sẽ được phép tấn công lượt đầu tiên
    - người thắng sẽ ko được hồi máu
17. trò chơi kết thúc khi chỉ còn 1 người chơi sống sót