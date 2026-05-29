# AddressMapperXML

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.app.address.mapper.AddressMapper">

    <resultMap id="BaseResultMap" type="com.example.app.address.model.entity.Address">
        <id column="id" property="id"/>
        <result column="user_id" property="userId"/>
        <result column="address_type" property="addressType"/>
        <result column="recipient_name" property="recipientName"/>
        <result column="recipient_phone" property="recipientPhone"/>
        <result column="country" property="country"/>
        <result column="province" property="province"/>
        <result column="city" property="city"/>
        <result column="district" property="district"/>
        <result column="street" property="street"/>
        <result column="postal_code" property="postalCode"/>
        <result column="is_default" property="isDefault"/>
        <result column="created_at" property="createdAt"/>
        <result column="created_by" property="createdBy"/>
        <result column="updated_at" property="updatedAt"/>
        <result column="updated_by" property="updatedBy"/>
        <result column="is_deleted" property="isDeleted"/>
    </resultMap>

    <sql id="BaseColumns">
        id, user_id, address_type, recipient_name, recipient_phone,
        country, province, city, district, street, postal_code,
        is_default, created_at, created_by, updated_at, updated_by, is_deleted
    </sql>

    <select id="selectPage" resultMap="BaseResultMap">
        SELECT * FROM (
            SELECT T.*, ROWNUM RN FROM (
                SELECT id, user_id, address_type, recipient_name, recipient_phone,
                       province, city, district, street, is_default, created_at, updated_at
                FROM addresses
                <where>
                    is_deleted = 0
                    <if test="userId != null and userId != ''">AND user_id = #{userId}</if>
                    <if test="addressType != null and addressType != ''">AND address_type = #{addressType}</if>
                    <if test="recipientName != null and recipientName != ''">AND recipient_name LIKE '%' || #{recipientName} || '%'</if>
                </where>
                ORDER BY is_default DESC, created_at DESC
            ) T WHERE ROWNUM &lt;= #{offset} + #{size}
        ) WHERE RN > #{offset}
    </select>

    <select id="count" resultType="Long">
        SELECT COUNT(*)
        FROM addresses
        <where>
            is_deleted = 0
            <if test="userId != null and userId != ''">AND user_id = #{userId}</if>
            <if test="addressType != null and addressType != ''">AND address_type = #{addressType}</if>
            <if test="recipientName != null and recipientName != ''">AND recipient_name LIKE '%' || #{recipientName} || '%'</if>
        </where>
    </select>

    <select id="selectById" resultMap="BaseResultMap">
        SELECT <include refid="BaseColumns"/>
        FROM addresses WHERE id = #{id} AND is_deleted = 0
    </select>

    <select id="selectByUserId" resultMap="BaseResultMap">
        SELECT <include refid="BaseColumns"/>
        FROM addresses WHERE user_id = #{userId} AND is_deleted = 0
        ORDER BY is_default DESC, created_at DESC
    </select>

    <select id="countByUserId" resultType="Long">
        SELECT COUNT(*) FROM addresses WHERE user_id = #{userId} AND is_deleted = 0
    </select>

    <select id="selectUpdatedAtById" resultType="java.time.LocalDateTime">
        SELECT updated_at FROM addresses WHERE id = #{id}
    </select>

    <insert id="insert">
        INSERT INTO addresses (id, user_id, address_type, recipient_name, recipient_phone,
                               country, province, city, district, street, postal_code,
                               is_default, created_at, created_by, updated_at, updated_by, is_deleted)
        VALUES (#{id}, #{userId}, #{addressType}, #{recipientName}, #{recipientPhone},
                #{country}, #{province}, #{city}, #{district, jdbcType=VARCHAR}, #{street}, #{postalCode, jdbcType=VARCHAR},
                #{isDefault, jdbcType=INTEGER}, SYSTIMESTAMP, #{createdBy}, SYSTIMESTAMP, #{createdBy}, 0)
    </insert>

    <update id="updateByIdAndUpdatedAt">
        UPDATE addresses
        <set>
            <if test="addressType != null">address_type = #{addressType},</if>
            <if test="recipientName != null">recipient_name = #{recipientName},</if>
            <if test="recipientPhone != null">recipient_phone = #{recipientPhone},</if>
            <if test="country != null">country = #{country},</if>
            <if test="province != null">province = #{province},</if>
            <if test="city != null">city = #{city},</if>
            <if test="district != null">district = #{district},</if>
            <if test="street != null">street = #{street},</if>
            <if test="postalCode != null">postal_code = #{postalCode},</if>
            <if test="isDefault != null">is_default = #{isDefault, jdbcType=INTEGER},</if>
            updated_at = SYSTIMESTAMP,
            updated_by = #{updatedBy}
        </set>
        WHERE id = #{id} AND updated_at = #{updatedAt}
    </update>

    <update id="logicDeleteByIdAndUpdatedAt">
        UPDATE addresses
        SET is_deleted = 1, updated_at = SYSTIMESTAMP, updated_by = #{updatedBy}
        WHERE id = #{id} AND updated_at = #{updatedAt}
    </update>

    <update id="updateIsDefaultByUserId">
        UPDATE addresses
        SET is_default = #{isDefault, jdbcType=INTEGER}
        WHERE user_id = #{userId} AND is_deleted = 0
    </update>

    <update id="updateIsDefaultById">
        UPDATE addresses
        SET is_default = #{isDefault, jdbcType=INTEGER},
            updated_at = SYSTIMESTAMP,
            updated_by = #{updatedBy}
        WHERE id = #{id} AND is_deleted = 0
    </update>

</mapper>
```
