package com.example.model;

import com.example.model.Product;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    @GET
    public Response getAll() {
        List<Product> products = Product.listAll();
        return Response.ok(products).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        Product product;
        try {
            product = Product.findById(new ObjectId(id));
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid product ID format: " + ex.getMessage())
                    .build();
        }

        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Product not found for ID: " + id)
                    .build();
        }

        return Response.ok(product).build();
    }

    @POST
    public Response create(Product product) {
        product.persist();
        return Response.ok(product).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, Product updated) {
        Product product;
        try {
            product = Product.findById(new ObjectId(id));
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid product ID format: " + ex.getMessage())
                    .build();
        }

        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Product not found for ID: " + id)
                    .build();
        }

        product.name = updated.name;
        product.price = updated.price;
        product.quantity = updated.quantity;
        product.update();

        return Response.ok(product).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Product product;
        try {
            product = Product.findById(new ObjectId(id));
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid product ID format: " + ex.getMessage())
                    .build();
        }

        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Product not found for ID: " + id)
                    .build();
        }

        product.delete();
        return Response.ok("Product deleted successfully").build();
    }

    @GET
    @Path("/low-stock")
    public Response lowStock(@QueryParam("threshold") @DefaultValue("5") int threshold) {
        List<Product> allProducts = Product.listAll();
        List<Product> lowStockProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.quantity < threshold) {
                lowStockProducts.add(product);
            }
        }

        return Response.ok(lowStockProducts).build();
    }

    @GET
    @Path("/search")
    public Response search(@QueryParam("name") String name) {
        List<Product> allProducts = Product.listAll();
        List<Product> matchedProducts = new ArrayList<>();

        if (name == null || name.isEmpty()) {
            return Response.ok(allProducts).build();
        }

        String lowerName = name.toLowerCase();

        for (Product product : allProducts) {
            if (product.name != null && product.name.toLowerCase().contains(lowerName)) {
                matchedProducts.add(product);
            }
        }

        return Response.ok(matchedProducts).build();
    }
}
