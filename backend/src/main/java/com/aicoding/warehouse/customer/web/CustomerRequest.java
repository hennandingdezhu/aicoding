package com.aicoding.warehouse.customer.web;

public record CustomerRequest(String customerCode, String customerName, String contactPerson, String contactPhone, String address, String remark) {}
