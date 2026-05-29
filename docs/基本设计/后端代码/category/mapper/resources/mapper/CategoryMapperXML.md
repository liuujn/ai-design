# CategoryMapperXML

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.app.category.mapper.CategoryMapper">

    <resultMap id="BaseResultMap" type="com.example.app.category.model.entity.Category">
        <id column="id" property="id"/>
        <result column="name" property="name"/>
        <result column="description" property="description"/>
        <result column="sort_order" property="sortOrder"/>
        <result column="status" property="status"/>
        <result column="created_at" property="createdAt"/>
        <result column="created_by" property="createdBy"/>
        <result column="updated_at" property="updatedAt"/>
        <result column="updated_by" property="updatedBy"/>
        <result column="is_deleted" property="isDeleted"/>
    </resultMap>

    <sql id="BaseColumns">
        id, name, description, sort_order, status,
        created_at, created_by, updated_at, updated_by, is_deleted
    </sql>

    <select id="selectPage" resultMap="BaseResultMap">
        SELECT * FROM (
            SELECT T.*, ROWNUM RN FROM (
                SELECT id, name, description, sort_order, status, created_at, updated_at
                FROM categories
                <where>
                    is_deleted = 0
                    <if test="keyword != null and keyword != ''">
                        AND (name LIKE '%' || #{keyword} || '%' OR description LIKE '%' || #{keyword} || '%')
                    </if>
                    <if test="status != null and status != ''">AND status = #{status}</if>
                </where>
                ORDER BY sort_order ASC, created_at DESC
            ) T WHERE ROWNUM &lt;= #{offset} + #{size}
        ) WHERE RN > #{offset}
    </select>

    <select id="count" resultType="Long">
        SELECT COUNT(*) FROM categories
        <where>
            is_deleted = 0
            <if test="keyword != null and keyword != ''">
                AND (name LIKE '%' || #{keyword} || '%' OR description LIKE '%' || #{keyword} || '%')
            </if>
            <if test="status != null and status != ''">AND status = #{status}</if>
        </where>
    </select>

    <select id="selectById" resultMap="BaseResultMap">
        SELECT <include refid="BaseColumns"/>
        FROM categories WHERE id = #{id} AND is_deleted = 0
    </select>

    <select id="selectUpdatedAtById" resultType="java.time.LocalDateTime">
        SELECT updated_at FROM categories WHERE id = #{id}
    </select>

    <select id="selectAllActive" resultMap="BaseResultMap">
        SELECT id, name, sort_order
        FROM categories
        WHERE status = 'active' AND is_deleted = 0
        ORDER BY sort_order ASC, name ASC
    </select>

    <insert id="insert">
        INSERT INTO categories (id, name, description, sort_order, status,
                                created_at, created_by, updated_at, updated_by, is_deleted)
        VALUES (#{id}, #{name}, #{description, jdbcType=VARCHAR}, #{sortOrder, jdbcType=INTEGER},
                #{status}, SYSTIMESTAMP, #{createdBy}, SYSTIMESTAMP, #{createdBy}, 0)
    </insert>

    <update id="updateByIdAndUpdatedAt">
        UPDATE categories
        <set>
            <if test="name != null">name = #{name},</if>
            <if test="description != null">description = #{description},</if>
            <if test="sortOrder != null">sort_order = #{sortOrder},</if>
            <if test="status != null">status = #{status},</if>
            updated_at = SYSTIMESTAMP,
            updated_by = #{updatedBy}
        </set>
        WHERE id = #{id} AND updated_at = #{updatedAt}
    </update>

    <update id="logicDeleteByIdAndUpdatedAt">
        UPDATE categories
        SET is_deleted = 1, updated_at = SYSTIMESTAMP, updated_by = #{updatedBy}
        WHERE id = #{id} AND updated_at = #{updatedAt}
    </update>

</mapper>
```
