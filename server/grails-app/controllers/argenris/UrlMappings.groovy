package argenris

class UrlMappings {
        // documentacion en: https://docs.grails.org/latest/guide/theWebLayer.html#urlmappings
    
    static mappings = {
        //voy a desactivar todos los mappings automaticos
      
       /*
        delete "/$controller/$id(.$format)?"(action:"delete")
      
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")
       
        
          get "/$controller(.$format)?"(action:"index")
         get "/$controller/$id(.$format)?"(action:"show")
    */
    
       
       //variante 1
        
        get "/ordenes"(controller:"orden", action:"index")
      //  get "/orden/create"(controller:"orden", action:"create")
        post "/ordenes"(controller:"orden", action:"save")
        get "/ordenes/$id"(controller:"orden", action:"show")
      //  get "/orden/$id/edit"(controller:"v", action:"edit")
      //  put "/orden/$id"(controller:"orden", action:"update")
      //  delete "/orden/$id"(controller:"orden", action:"delete")
        
    
        
        //Variante 2
      //  "/ordenes"(resources:'orden', excludes:['delete', 'update', 'edit','create'])
    
    
        //Variante
         "/pacientes"(resources:'paciente', excludes:['delete', 'update', 'edit','create'])
        
        // ojo, no tiene que existir el save cita, por que eso se crea por la orden.
         "/citas"(resources:'cita', excludes:['save','delete', 'update', 'edit','create'])
        
    
        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
