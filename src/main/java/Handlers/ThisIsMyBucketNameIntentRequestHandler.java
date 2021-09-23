package Handlers;

import Utils.Util;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static Utils.Util.fallbackResponse;
import static Utils.Util.getSimpleResponse;
import static com.amazon.ask.request.Predicates.intentName;

public class ThisIsMyBucketNameIntentRequestHandler implements RequestHandler
{
    @Override
    public boolean canHandle(HandlerInput input)
    {
        return input.matches(intentName("ThisIsMyBucketNameIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input)
    {
        if (Util.supportsApl(input))
        {
            if (input.getRequestEnvelope().getRequest().getType().equals("IntentRequest"))
            {
                Intent intent = ((IntentRequest)input.getRequestEnvelope().getRequest()).getIntent();

                if (intent != null)
                {
                    String intentName = intent.getName();

                    if (intentName != null)
                    {
                        if (input.getAttributesManager().getSessionAttributes().getOrDefault("task_name",null) == null)
                        {
                            String title = "task name empty";

                            String message = "Sorry, i could not find your task name. So please, first say the task name with the keyword task name.";

                            Map<String,Object> session = new HashMap<>();

                            session.put("repeat_message",message);
                            session.put("repeat_re_prompt_message",message);

                            return getSimpleResponse(input,title,message,message,message,session);
                        }
                        else
                        {
                            String taskName = input.getAttributesManager().getSessionAttributes().get("task_name").toString();

                            if (input.getAttributesManager().getSessionAttributes().getOrDefault("bucket_name",null) == null)
                            {
                                String title = "bucket name empty";

                                String message = "Sorry, i could not find your bucket name. So please, first say the bucket name with the keyword bucket name.";

                                Map<String,Object> session = new HashMap<>();

                                session.put("task_name",taskName);
                                session.put("repeat_message",message);
                                session.put("repeat_re_prompt_message",message);

                                return getSimpleResponse(input,title,message,message,message,session);
                            }
                            else
                            {
                                String bucketName = input.getAttributesManager().getSessionAttributes().get("bucket_name").toString();

                                String title = "say file name";

                                String message = "Okay, now say the file name of your file with the keyword file name. Example, file name apple.";

                                Map<String,Object> session = new HashMap<>();

                                session.put("task_name",taskName);
                                session.put("bucket_name",bucketName);
                                session.put("repeat_message",message);
                                session.put("repeat_re_prompt_message",message);

                                return getSimpleResponse(input,title,message,message,message,session);
                            }
                        }
                    }
                    else
                    {
                        return fallbackResponse(input);
                    }
                }
                else
                {
                    return fallbackResponse(input);
                }
            }
            else
            {
                return fallbackResponse(input);
            }
        }
        else
        {
            return input.getResponseBuilder()
                    .withSpeech(Util.unSupportDeviceFallbackMessage)
                    .build();
        }
    }
}
