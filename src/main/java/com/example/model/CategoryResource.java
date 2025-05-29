package com.example.model;

import com.example.model.Category;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.List;

@Path("/categories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @GET
    public Response getAll() {
        List<Category> categories = Category.listAll();
        return Response.ok(categories).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        Category category;
        try {
            category = Category.findById(new ObjectId(id));
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid category ID format: " + ex.getMessage())
                    .build();
        }

        if (category == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Category not found for ID: " + id)
                    .build();
        }

        return Response.ok(category).build();
    }

    @POST
    public Response create(Category category) {
        category.persist();
        return Response.ok(category).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, Category updated) {
        Category category;
        try {
            category = Category.findById(new ObjectId(id));
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid category ID format: " + ex.getMessage())
                    .build();
        }

        if (category == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Category not found for ID: " + id)
                    .build();
        }

        category.name = updated.name;
        category.description = updated.description;
        category.update();

        return Response.ok(category).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Category category;
        try {
            category = Category.findById(new ObjectId(id));
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid category ID format: " + ex.getMessage())
                    .build();
        }

        if (category == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Category not found for ID: " + id)
                    .build();
        }

        category.delete();
        return Response.ok("Category deleted successfully").build();
    }
}
