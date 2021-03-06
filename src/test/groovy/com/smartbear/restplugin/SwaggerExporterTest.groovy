package com.smartbear.restplugin;

import com.eviware.soapui.impl.rest.RestService
import com.eviware.soapui.impl.wsdl.WsdlProject
import com.smartbear.swagger4j.Constants
import com.smartbear.swagger4j.Swagger

/**
 * Basic tests that use the examples at wordnik - if they change these tests will probably break
 * 
 * @author Ole Lensmar
 */

class SwaggerExporterTest extends GroovyTestCase {
	void testExportResourceListing() {
		def project = new WsdlProject();

        SwaggerImporter importer = new SwaggerImporter( project )

        RestService [] result = importer.importSwagger( "http://petstore.swagger.wordnik.com/api/api-docs.json" )

        assertEquals( 2, result.length )

        SwaggerExporter exporter = new SwaggerExporter( project );
        def listing = exporter.generateResourceListing( result, "1.0", Constants.Format.json, "" );
        listing.setBasePath( "." );

        def path = exporter.exportResourceListing( Constants.Format.json, listing, "target/test-export" );
        def file = new File( path )

        assertTrue( file.exists());
        assertTrue( path.endsWith( ".json"))

        def resourceListing = Swagger.createReader().readResourceListing(file.toURI())
        assertEquals( resourceListing.apiVersion, listing.apiVersion )
        assertEquals( resourceListing.apiList.size(), listing.apiList.size() )
	}
}
