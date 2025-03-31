package dao;

import model.Coupon;

public interface ICouponDAO {
    Coupon getCouponById(Integer couponId);
    double getCouponSale(Integer couponId);
}
